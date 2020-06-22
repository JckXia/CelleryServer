package com.cellery.api.backend.ui.service;

import com.cellery.api.backend.shared.RoutineDto;
import com.cellery.api.backend.shared.Util.MapperUtil;
import com.cellery.api.backend.ui.data.*;
import com.googlecode.gentyref.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
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

    @Autowired
    public RoutinesService(RoutinesRepository routinesRepository, ProductsRepository productsRepository, UsersRepository usersRepository,
                           MapperUtil mapper) {
        this.routinesRepository = routinesRepository;
        this.productsRepository = productsRepository;
        this.usersRepository = usersRepository;
        this.mapper = mapper;
    }

    private Type routineDtoListType() {
        return new TypeToken<List<RoutineDto>>(){}.getType();
    }

    public List<RoutineDto> getRoutines(String email) {
        List<RoutineEntity> routines = usersRepository.findByEmail(email).getRoutines();
        return mapper.strictMapper().map(routines, routineDtoListType());
    }

    // TODO: Fix problem with routine and products relationship not updating correctly in db
    public RoutineDto createRoutine(String email, List<String> addProducts) throws RuntimeException {
        RoutineEntity routineEntity = new RoutineEntity();
        routineEntity.setRoutineId(UUID.randomUUID().toString());

        // add user to routine
        UserEntity userEntity = usersRepository.getOneByEmail(email); // get ref to db object
        routineEntity.setUser(userEntity); // user is UserEntity type; not PersistentBag type (which won't save the routine properly)
        routinesRepository.save(routineEntity);

        // TODO: SAME FOREIGN KEY CONSTRAINT ERR ABOUT ROUTINE ID NOT EXISTING? ALTHOUGH IT DOES, NEEDS FIX
        // get product entities
        List<ProductEntity> productEntities = addProducts.stream().map(wrap(id -> productsRepository.findByProductId(id))).collect(Collectors.toList());
        RoutineEntity savedRoutine = routinesRepository.getOneByRoutineId(routineEntity.getRoutineId());
        savedRoutine.setProducts(productEntities); // add existing parent values (products) into existing child (routine)
        routinesRepository.save(savedRoutine);

        RoutineDto returnDto = mapper.strictMapper().map(routineEntity, RoutineDto.class);
        return returnDto;
    }

}
