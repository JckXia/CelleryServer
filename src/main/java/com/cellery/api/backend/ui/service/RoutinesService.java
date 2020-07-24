package com.cellery.api.backend.ui.service;

import com.cellery.api.backend.shared.RoutineDto;
import com.cellery.api.backend.shared.Util.BelongsToUserUtil;
import com.cellery.api.backend.shared.Util.MapperUtil;
import com.cellery.api.backend.ui.data.*;
import com.googlecode.gentyref.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.cellery.api.backend.shared.Util.CheckedFn.wrap;

@Service
public class RoutinesService {

    private RoutinesRepository routinesRepository;
    private ProductsRepository productsRepository;
    private UsersRepository usersRepository;
    private MapperUtil mapper;
    private BelongsToUserUtil userUtil;

    @Autowired
    public RoutinesService(RoutinesRepository routinesRepository, ProductsRepository productsRepository, UsersRepository usersRepository,
                           MapperUtil mapper, BelongsToUserUtil userUtil) {
        this.routinesRepository = routinesRepository;
        this.productsRepository = productsRepository;
        this.usersRepository = usersRepository;
        this.mapper = mapper;
        this.userUtil = userUtil;
    }

    private Type routineDtoListType() {
        return new TypeToken<List<RoutineDto>>() {}.getType();
    }


    public List<RoutineDto> getRoutines(String email) {
        List<RoutineEntity> routines = usersRepository.findByEmail(email).getRoutines();
        return mapper.strictMapper().map(routines, routineDtoListType());
    }

    @Transactional // do in one db transaction
    public RoutineDto createRoutine(String email, List<String> addProducts, Boolean isAm) throws RuntimeException, FileNotFoundException {
        if (addProducts.isEmpty()) {
            throw new FileNotFoundException("Cannot create routine with no products");
        }

        // check if products belong to user
        userUtil.productsBelongToUser(email, addProducts);

        RoutineEntity routineEntity = new RoutineEntity();
        routineEntity.setRoutineId(UUID.randomUUID().toString());
        routineEntity.setIsAm(isAm);

        // add user to routine
        UserEntity userEntity = usersRepository.getOneByEmail(email); // get ref to db object
        routineEntity.setUser(userEntity); // user is UserEntity type; not PersistentBag type (which won't save the routine properly)
        routinesRepository.save(routineEntity);

        /// We must get the freshly saved routine entity to set it's products because otherwise, there is no entry
        // in the db belonging to the routine, so by setting the products field on routineEntity will create
        // foreign key constraint error on routine entity (as it does not exist in the db)
        List<ProductEntity> productEntities = addProducts.stream().map(wrap(id -> productsRepository.getOneByProductId(id))).collect(Collectors.toList());
        RoutineEntity savedRoutine = routinesRepository.getOneByRoutineId(routineEntity.getRoutineId());
        savedRoutine.setProducts(productEntities); // add existing child values (products) into existing parent (routine)

        // add routine to each product and save
        for (ProductEntity product : productEntities) {
            product.addRoutine(savedRoutine);
            productsRepository.save(product);
        }

        routinesRepository.save(savedRoutine); // save routine to db again, this time with products

        RoutineDto returnDto = mapper.strictMapper().map(routineEntity, RoutineDto.class);
        return returnDto;
    }

    // deleting a routine DOES NOT delete the products in the routine
    // the products still exist in the db as they are separate from routines
    public void deleteRoutine(String email, String deleteRoutineId) throws FileNotFoundException {
        if (!routinesRepository.existsByRoutineId(deleteRoutineId) || !userUtil.routineBelongsToUser(email, deleteRoutineId)) {
            throw new FileNotFoundException("Routine does not exist");
        }

        RoutineEntity toDelete = routinesRepository.getOneByRoutineId(deleteRoutineId);

        UserEntity user = toDelete.getUser();
        user.removeRoutineFromUser(toDelete); // remove routine from user so routine can be deleted from db succesfully
        usersRepository.save(user);

        routinesRepository.delete(toDelete);
    }

    // set routine's products with the new products
    public RoutineDto updateProductsInRoutine(String email, String routineId, List<String> products) throws FileNotFoundException {
        if (!routinesRepository.existsByRoutineId(routineId) || !userUtil.routineBelongsToUser(email, routineId)) {
            throw new FileNotFoundException("Routine does not exist");
        }

        if (products.size() == 0) {
            throw new FileNotFoundException("Trying to update into an empty routine");
        }

        userUtil.productsBelongToUser(email, products);

        RoutineEntity routine = routinesRepository.getOneByRoutineId(routineId);

        List<ProductEntity> productEntities = new ArrayList<>();
        for (String productId : products) {
            productEntities.add(productsRepository.findByProductId(productId));
        }

        routine.setProducts(productEntities);
        routinesRepository.save(routine);

        RoutineDto returnDto = mapper.strictMapper().map(routine, RoutineDto.class);
        return returnDto;
    }

    // products still EXIST in db they ARE NOT DELETED FROM THE DB EVEN IF REMOVED FROM A ROUTINE
    public RoutineDto removeProductsFromRoutine(String email, String routineId, List<String> toRemove) throws FileNotFoundException {

        if (!routinesRepository.existsByRoutineId(routineId) || !userUtil.routineBelongsToUser(email, routineId)) {
            throw new FileNotFoundException("Routine does not exist");
        }

        userUtil.productsBelongToUser(email, toRemove); // check if user actually has these products

        RoutineEntity editRoutine = routinesRepository.getOneByRoutineId(routineId);

        if (editRoutine.getProducts().size() == toRemove.size()) { // this is basically same thing as deleting a routine
            routinesRepository.delete(editRoutine);
            return null;
        }

        // otherwise, the modified routine will still contain at least 1 product, so we have to save the routine after
        // removing the products

        // if a product does not exist, we do not throw an exception as the user intends to remove products from the routine
        for (String productId : toRemove) {
            if (productsRepository.existsByProductId(productId)) {
                ProductEntity product = productsRepository.findByProductId(productId); // we dont need to get ref to db object
                editRoutine.removeProductFromRoutine(product);
            }
        }

        routinesRepository.save(editRoutine);
        RoutineDto returnDto = mapper.strictMapper().map(editRoutine, RoutineDto.class);
        return returnDto;
    }

    public RoutineDto addProductsToRoutine(String email, String routineId, List<String> toAdd) throws FileNotFoundException {
        if (!routinesRepository.existsByRoutineId(routineId) || !userUtil.routineBelongsToUser(email, routineId)) {
            throw new FileNotFoundException("Routine does not exist");
        }

        userUtil.productsBelongToUser(email, toAdd); // check if user actually has these products

        RoutineEntity editRoutine = routinesRepository.getOneByRoutineId(routineId);

        for (String productId: toAdd) {
            if (!productsRepository.existsByProductId(productId)) {
                throw new FileNotFoundException("Product does not exist");
            }
            ProductEntity product = productsRepository.findByProductId(productId); // not ref to db object
            editRoutine.addProductToRoutine(product);
        }

        // at this point, all products added exist so we can safely save the updated routine entity
        routinesRepository.save(editRoutine);
        RoutineDto returnDto = mapper.strictMapper().map(editRoutine, RoutineDto.class);
        return returnDto;
    }
}
