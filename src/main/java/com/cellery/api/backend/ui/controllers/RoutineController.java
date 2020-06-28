package com.cellery.api.backend.ui.controllers;

import com.cellery.api.backend.shared.RoutineDto;
import com.cellery.api.backend.shared.UserDto;
import com.cellery.api.backend.shared.Util.JwtUtil;
import com.cellery.api.backend.shared.Util.MapperUtil;
import com.cellery.api.backend.shared.Util.Utils;
import com.cellery.api.backend.ui.model.request.ProductsInRoutineRequestModel;
import com.cellery.api.backend.ui.model.response.RoutineRespModel;
import com.cellery.api.backend.ui.service.RoutinesService;
import com.cellery.api.backend.ui.service.UsersService;
import com.googlecode.gentyref.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.List;

@RestController
@RequestMapping(path = "/routines")
public class RoutineController {

    private Environment env;
    private RoutinesService routinesService;
    private UsersService usersService;
    private MapperUtil mapper;
    private JwtUtil jwtUtil;
    private Utils utils;

    @Autowired
    RoutineController(Environment env, RoutinesService rs, UsersService us, MapperUtil mapper, JwtUtil jwtUtil, Utils utils) {
        this.env = env;
        routinesService = rs;
        usersService = us;
        this.mapper = mapper;
        this.jwtUtil = jwtUtil;
        this.utils = utils;
    }

    private Type routineRespModelListType() {
        return new TypeToken<List<RoutineRespModel>>(){}.getType();
    }

    private UserDto getUserDto(String auth) throws UsernameNotFoundException {
        String email = jwtUtil.getEmailFromToken(auth);
        UserDto userDto = usersService.getUserDetailsByEmail(email);
        return userDto;
    }

    // get all routines
    @GetMapping
    public ResponseEntity<List<RoutineRespModel>> getRoutines(@RequestHeader(value = "${authentication.authorization}") String auth) {

        if (utils.emptyStr(auth)) { // if token is empty/null
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(null);
        }

        try {
            auth = auth.replace(env.getProperty("authentication.bearer"), "");
            UserDto userDto = getUserDto(auth);

            if (!jwtUtil.validateToken(auth, userDto)) {
                return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(null);
            }

            List<RoutineDto> usersRoutines = routinesService.getRoutines(jwtUtil.getEmailFromToken(auth));
            List<RoutineRespModel> returnVal = mapper.strictMapper().map(usersRoutines, routineRespModelListType());
            return ResponseEntity.status(HttpStatus.OK).body(returnVal);

        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } // jwt related exceptions are caught by the web filter
    }

    // create routine
    @PostMapping
    public ResponseEntity<RoutineRespModel> createRoutine(@RequestHeader(value = "${authentication.authorization}") String auth,
                                @Valid @RequestBody ProductsInRoutineRequestModel createRoutine) {

        if (utils.emptyStr(auth) || createRoutine.getProductIds().isEmpty()) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(null);
        }

        try {
            auth = auth.replace(env.getProperty("authentication.bearer"), "");
            UserDto userDto = getUserDto(auth);

            if (!jwtUtil.validateToken(auth, userDto) || userDto.getRoutines().size() == 2) { // max routines a user can have is 2
                return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(null);
            }

            // create routine and add it to user
            RoutineDto createdDto = routinesService.createRoutine(jwtUtil.getEmailFromToken(auth), createRoutine.getProductIds());

            RoutineRespModel returnVal = mapper.strictMapper().map(createdDto, RoutineRespModel.class);
            return ResponseEntity.status(HttpStatus.CREATED).body(returnVal);

        } catch (RuntimeException e) {
            // the user trying to create routine does not exist,
            // user has valid jwt but email does not match the valid user, or
            // user is adding a nonexistent product to new routine
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // delete routine by id
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deleteRoutine(@PathVariable String id) {
        try {
            routinesService.deleteRoutine(id);
            return ResponseEntity.status(HttpStatus.OK).body("Successfully deleted routine");

        } catch (FileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // remove products from routine
    @PatchMapping(path = "/{id}")
    public ResponseEntity<RoutineRespModel> removeProductsFromRoutine(@PathVariable String id,
                                                                      @Valid @RequestBody ProductsInRoutineRequestModel productsToRemove) {
        try {
            RoutineDto returnDto = routinesService.removeProductsFromRoutine(id, productsToRemove.getProductIds());

            if (returnDto != null) {
                RoutineRespModel returnVal = mapper.strictMapper().map(returnDto, RoutineRespModel.class);
                return ResponseEntity.status(HttpStatus.OK).body(returnVal);
            }
            return ResponseEntity.status(HttpStatus.OK).body(null);

        } catch (FileNotFoundException e) {
            // when routine does not exist
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // add products to routine
    @PatchMapping(path = "/{id}")
    public ResponseEntity<RoutineRespModel> addProductsToRoutine(@PathVariable String id,
                                                                 @Valid @RequestBody ProductsInRoutineRequestModel productsToAdd) {
        try {
            RoutineDto returnDto = routinesService.addProductsToRoutine(id, productsToAdd.getProductIds());

            RoutineRespModel returnVal = mapper.strictMapper().map(returnDto, RoutineRespModel.class);
            return ResponseEntity.status(HttpStatus.OK).body(returnVal);

        } catch (FileNotFoundException e) {
            // routine does not exist or a product does not exist
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
