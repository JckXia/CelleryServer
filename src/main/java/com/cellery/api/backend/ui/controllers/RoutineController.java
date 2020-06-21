package com.cellery.api.backend.ui.controllers;

import com.cellery.api.backend.shared.RoutineDto;
import com.cellery.api.backend.shared.UserDto;
import com.cellery.api.backend.shared.Util.JwtUtil;
import com.cellery.api.backend.shared.Util.MapperUtil;
import com.cellery.api.backend.ui.model.request.CreateRoutineRequestModel;
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

    private Boolean emptyStr(String str) {
        return str == null || str.isEmpty();
    }

    private Type routineRespModelListType() {
        return new TypeToken<List<RoutineRespModel>>(){}.getType();
    }

    private UserDto getUserDto(String auth) throws UsernameNotFoundException {
        String email = jwtUtil.getEmailFromToken(auth);
        UserDto userDto = usersService.getUserDetailsByEmail(email);
        return userDto;
    }

    @GetMapping
    public String test() {
        return "This is /routines endpoint";
    }
    // TODO: delete routine (all products will detach from routine), edit routine

    @GetMapping(path="/get")
    public ResponseEntity<List<RoutineRespModel>> getRoutines(@RequestHeader(value = "${authentication.authorization}") String auth) {

        if (emptyStr(auth)) { // if token is empty/null
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


    @PostMapping(path="/create")
    public ResponseEntity<RoutineRespModel> createRoutine(@RequestHeader(value = "${authentication.authorization}") String auth,
                                @Valid @RequestBody CreateRoutineRequestModel createRoutine) {

        if (emptyStr(auth) || createRoutine.getProducts().isEmpty()) {
            return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(null);
        }

        try {
            auth = auth.replace(env.getProperty("authentication.bearer"), "");
            UserDto userDto = getUserDto(auth);

            if (!jwtUtil.validateToken(auth, userDto) || userDto.getRoutines().size() == 2) { // max routines a user can have is 2
                return ResponseEntity.status(HttpStatus.PRECONDITION_FAILED).body(null);
            }

            // create routine and add it to user
            RoutineDto createdDto = routinesService.createRoutine(jwtUtil.getEmailFromToken(auth), createRoutine.getProducts());

            RoutineRespModel returnVal = mapper.strictMapper().map(createdDto, RoutineRespModel.class);
            return ResponseEntity.status(HttpStatus.CREATED).body(returnVal);

        } catch (RuntimeException e) {
            // the user trying to create routine does not exist,
            // user has valid jwt but email does not match the valid user, or
            // user is adding a nonexistent product to new routine
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /*
    @PatchMapping(path = "/edit")
    public ResponseEntity<> removeProducts(@RequestHeader(value = "${authentication.authorization}") String auth,
                                                                         @Valid @RequestBody  removeProducts) {
        // TODO
    }*/
}
