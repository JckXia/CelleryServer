package com.cellery.api.backend.ui.controllers;

import com.cellery.api.backend.shared.RoutineDto;
import com.cellery.api.backend.shared.UserDto;
import com.cellery.api.backend.shared.Util.JwtUtil;
import com.cellery.api.backend.shared.Util.MapperUtil;
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

    @Autowired
    RoutineController(Environment env, RoutinesService rs, UsersService us, MapperUtil mapper, JwtUtil jwtUtil) {
        this.env = env;
        routinesService = rs;
        usersService = us;
        this.mapper = mapper;
        this.jwtUtil = jwtUtil;
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

        } catch (FileNotFoundException | RuntimeException e) {
            // the user trying to create routine does not exist,
            // user has valid jwt but email does not match the valid user,
            // user is adding a nonexistent product to new routine, or
            // user does not own the products to add to the new routine
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // delete routine by id
    @DeleteMapping(path = "/{id}")
    public ResponseEntity<String> deleteRoutine(@PathVariable String id, @RequestHeader(value = "${authentication.authorization}") String auth) {
        try {
            auth = auth.replace(env.getProperty("authentication.bearer"), "");
            UserDto userDto = getUserDto(auth);

            if (!jwtUtil.validateToken(auth, userDto)) { // max routines a user can have is 2
                return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(null);
            }

            routinesService.deleteRoutine(jwtUtil.getEmailFromToken(auth), id);
            return ResponseEntity.status(HttpStatus.OK).body("Successfully deleted routine");

        } catch (UsernameNotFoundException | FileNotFoundException e) { // user or routine does not exist
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // remove products from routine
    @PatchMapping(path = "/remove-products/{id}")
    public ResponseEntity<RoutineRespModel> removeProductsFromRoutine(@PathVariable String id,
                                                                      @Valid @RequestBody ProductsInRoutineRequestModel productsToRemove,
                                                                      @RequestHeader(value = "${authentication.authorization}") String auth) {
        try {
            auth = auth.replace(env.getProperty("authentication.bearer"), "");
            UserDto userDto = getUserDto(auth);

            if (!jwtUtil.validateToken(auth, userDto)) { // max routines a user can have is 2
                return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(null);
            }

            RoutineDto returnDto = routinesService.removeProductsFromRoutine(jwtUtil.getEmailFromToken(auth), id, productsToRemove.getProductIds());

            if (returnDto != null) {
                RoutineRespModel returnVal = mapper.strictMapper().map(returnDto, RoutineRespModel.class);
                return ResponseEntity.status(HttpStatus.OK).body(returnVal);
            }
            return ResponseEntity.status(HttpStatus.OK).body(null);

        } catch (UsernameNotFoundException | FileNotFoundException e) {
            // when user or routine does not exist
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // add products to routine
    @PatchMapping(path = "/add-products/{id}")
    public ResponseEntity<RoutineRespModel> addProductsToRoutine(@PathVariable String id,
                                                                 @Valid @RequestBody ProductsInRoutineRequestModel productsToAdd,
                                                                 @RequestHeader(value = "${authentication.authorization}") String auth) {
        try {
            auth = auth.replace(env.getProperty("authentication.bearer"), "");
            UserDto userDto = getUserDto(auth);

            RoutineDto returnDto = routinesService.addProductsToRoutine(jwtUtil.getEmailFromToken(auth), id, productsToAdd.getProductIds());

            RoutineRespModel returnVal = mapper.strictMapper().map(returnDto, RoutineRespModel.class);
            return ResponseEntity.status(HttpStatus.OK).body(returnVal);

        } catch (FileNotFoundException e) {
            // routine does not exist or a product does not exist
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
