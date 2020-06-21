package com.cellery.api.backend.ui.controllers;

import com.cellery.api.backend.ui.service.RoutinesService;
import com.cellery.api.backend.ui.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/routines")
public class RoutineController {

    private Environment env;
    private RoutinesService routinesService;
    private UsersService usersService;

    @Autowired
    RoutineController(Environment env, RoutinesService rs, UsersService us) {
        this.env = env;
        routinesService = rs;
        usersService = us;
    }

    // TODO: create RemoveRoutine<Resp/Req>Model and CreateRoutine<Resp/Req>Model

    /*@GetMapping
    public ResponseEntity<RoutineRespModel> getAllRoutines(@RequestHeader(value = "${authentication.authorization}") HttpHeaders headers) {
        TODO
    }

    @PostMapping(path="/create")
    @ResponseStatus(HttpStatus.CREATED) // change this
    public ResponseEntity<CreateRoutineRespModel> create(@RequestHeader(value = "${authentication.authorization}") String auth,
                                @Valid @RequestBody CreateRoutineReqModel createRoutineReqModel) {

        if (auth == null || auth.isEmpty()) {
            // TODO
        }
        String token = auth.replace(env.getProperty("authentication.bearer"), "");
        String user = Jwts.parser().setSigningKey(env.getProperty("authentication.jwt.secret")).parseClaimsJws(token).getBody().getSubject();

        if (user == null || user.isEmpty()) {
          // TODO
        }

        // check if user can create new routine
        UserDto userInfo = usersService.getUserDetailsByEmail(user);
        if (userInfo.getRoutines().size() == 2) {
            // TODO
        }

        // can create new routine
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        RoutineDto routineDto = mapper.map(createRoutineReqModel, RoutineDto.class);
        RoutineDto createRoutineResp = routinesService.createRoutine(routineDto);
        CreateRoutineRespModel returnVal = new ModelMapper().map(createRoutineResp, CreateRoutineRespModel.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(returnVal);
    }

    @Patch(path = "/remove-products")
    public ResponseEntity<RemoveRoutineProductsRespModel> removeProducts(@RequestHeader(value = "${authentication.authorization}") String auth,
                                                                         @Valid @RequestBody RemoveRoutineProductsReqModel removeProducts) {
        // TODO
    }*/
}
