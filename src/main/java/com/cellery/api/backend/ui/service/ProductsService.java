package com.cellery.api.backend.ui.service;

import com.cellery.api.backend.shared.ProductDto;
import com.cellery.api.backend.shared.Util.BelongsToUserUtil;
import com.cellery.api.backend.shared.Util.MapperUtil;
import com.cellery.api.backend.ui.data.*;
import com.googlecode.gentyref.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;

@Service
public class ProductsService {

    private ProductsRepository productsRepository;
    private RoutinesRepository routinesRepository;
    private UsersRepository usersRepository;
    private MapperUtil mapper;
    private BelongsToUserUtil userUtil;

    @Autowired
    public ProductsService(ProductsRepository productsRepository, RoutinesRepository routinesRepository,
                           UsersRepository usersRepository, MapperUtil mapper, BelongsToUserUtil userUtil) {
        this.productsRepository = productsRepository;
        this.routinesRepository = routinesRepository;
        this.usersRepository = usersRepository;
        this.mapper = mapper;
        this.userUtil = userUtil;
    }

    /*private ProductDto convertEntityToDto(ProductEntity src) {
        Converter<List<RoutineEntity>, Integer> convert = arr -> arr.getSource() == null ? 0 : arr.getSource().size();

        ModelMapper lilMap = this.mapper.strictMapper();
        lilMap.createTypeMap(ProductEntity.class, ProductDto.class).addMappings(mapper ->
                mapper.using(convert).map(ProductEntity::getRoutines, ProductDto::setRoutines));

        return lilMap.map(src, ProductDto.class);
    }*/

    private Type productDtoList() {
        return new TypeToken<List<ProductDto>>() {}.getType();
    }

    public List<ProductDto> getProducts(String email) {
        List<ProductEntity> productEntities = usersRepository.findByEmail(email).getUserProducts();
        return mapper.strictMapper().map(productEntities, productDtoList());
    }

    public ProductDto getProduct(String email, String wantedProductId) throws FileNotFoundException {
        if (!productsRepository.existsByProductId(wantedProductId) || !userUtil.productBelongsToUser(email, wantedProductId)) {
            throw new FileNotFoundException("Product does not exist");
        }

        ProductEntity productEntity = productsRepository.findByProductId(wantedProductId);

        ProductDto returnDto = mapper.strictMapper().map(productEntity, ProductDto.class);
        return returnDto;
    }

    public ProductDto createProduct(String email, ProductDto newProduct) {
        newProduct.setProductId(UUID.randomUUID().toString());
        ProductEntity productEntity = mapper.strictMapper().map(newProduct, ProductEntity.class);

        // get user that product belongs to
        UserEntity productUser = usersRepository.getOneByEmail(email);
        productEntity.setProductUser(productUser);
        productsRepository.save(productEntity); // save to database

        ProductDto returnDto = mapper.strictMapper().map(productEntity, ProductDto.class);
        return returnDto;
    }

    // Deletes a product by the productId
    public void deleteProduct(String email, String deleteProductId) throws FileNotFoundException {
        if (!productsRepository.existsByProductId(deleteProductId) || !userUtil.productBelongsToUser(email, deleteProductId)) {
            throw new FileNotFoundException("Product does not exist");
        }

        ProductEntity productEntity = productsRepository.findByProductId(deleteProductId);

        // The product will be deleted regardless if it is in a routine or not. On the frontend, the user
        // will be prompted about the effects of deleting the product if it is in a routine (it will disappear from
        // routines it is in)

        // check if the routine(s) that have this product have only 1 product, so deleting the product
        // will delete that routine too
        List<RoutineEntity> inRoutines = productEntity.getRoutines();
        for (RoutineEntity routine: inRoutines) {
            if (routine.getProducts().size() == 1) {
                routinesRepository.delete(routine); // goodbye
            } else {
                // remove it from the routine
                routine.removeProductFromRoutine(productEntity); // if we do not, the product still exists in db even after line 92 since routine is the parent
                routinesRepository.save(routine);
            }
        }
       UserEntity user =  productEntity.getProductUser();
        user.removeProductFromUser(productEntity);
        usersRepository.save(user);
        productsRepository.delete(productEntity);
    }

    /* In a list of products to delete, if one product does not exist, we will
        continue iterating over the remaining products. An exception does not stop the loop.
        Returns the number of products deleted
     */
    public Integer deleteProducts(String email, List<String> ids) throws FileNotFoundException {
        Integer numDeleted = 0;

        // check if products belong to user
        userUtil.productsBelongToUser(email, ids);

        // any exceptions in the loop come from the product not existing in the db but still owned by the user
        for (String id: ids) {
            try {
                deleteProduct(email, id);
                ++numDeleted;
            } catch (FileNotFoundException e) {
                /* I know! This is strange and definitely not the cleanest way to deal with our case */
            }
        }
        return numDeleted;
    }

    public ProductDto editProduct(String email, ProductDto product) throws FileNotFoundException {
        if (!productsRepository.existsByProductId(product.getProductId()) || !userUtil.productBelongsToUser(email, product.getProductId())) {
            throw new FileNotFoundException("Product does not exist");
        }

        // get ref db object
        ProductEntity productEdit = productsRepository.getOneByProductId(product.getProductId());

        if (!product.getName().isEmpty() && product.getName() != null && !product.getName().equals(productEdit.getName())) {
            productEdit.setName(product.getName());
        }

        // product desc is allowed to be empty
        if (!product.getDescription().equals(productEdit.getDescription())) {
            productEdit.setDescription(product.getDescription());
        }

        ProductDto returnDto = mapper.strictMapper().map(productEdit, ProductDto.class);

        // save object changes to db
        productsRepository.save(productEdit);

        return returnDto;
    }
}
