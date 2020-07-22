package com.cellery.api.backend.ui.controllers;
import com.cellery.api.backend.shared.LogDto;
import com.cellery.api.backend.shared.UserDto;
import com.cellery.api.backend.shared.Util.JwtUtil;
import com.cellery.api.backend.shared.Util.MapperUtil;
import com.cellery.api.backend.ui.model.response.CreateLogRespModel;
import com.cellery.api.backend.ui.service.LogsService;
import com.cellery.api.backend.ui.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping(path = "/log")
public class LogController {

    private Environment env;
    private JwtUtil jwtUtil;
    private UsersService usersService;
    private LogsService logsService;
    private MapperUtil modelMapper;

    @Autowired
    LogController(Environment env, JwtUtil jwtUtil, UsersService us, LogsService ls, MapperUtil modelMapper) {
        this.env = env;
        this.jwtUtil = jwtUtil;
        this.usersService = us;
        this.logsService = ls;
        this.modelMapper = modelMapper;
    }

    // /log
    @GetMapping
    public String testReturn() {
        return "testRest";
    }

    @PostMapping
    public ResponseEntity<CreateLogRespModel> createLog(@RequestHeader(value = "${authentication.authorization}") String auth) {

        try {
            String authToken = auth.replace(env.getProperty("authentication.bearer"), "");
            UserDto userDto = getUserDto(authToken);
            if (!jwtUtil.validateToken(authToken, userDto)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            LogDto logCreationObject = logsService.createLog(userDto.getEmail());
            // We need to get the userDto (current user, and create it using a serviceImpl)
            CreateLogRespModel returnModel = modelMapper.strictMapper().map(logCreationObject, CreateLogRespModel.class);
            return ResponseEntity.status(HttpStatus.CREATED).body(returnModel);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping(path="/{log_id}")
    public String updateLog( @PathVariable String log_id, @RequestHeader(value = "${authentication.authorization}") String auth){

        // Inside this controller, we have to verify
        // 1. The log_id exists
        //      (The log that we are attempting to edit is today's log), else we want to reject
        // 2. THe data that we are attempting to update is valid
        //    Possible data that we have to update:
        //    Am/pm routine  // This will be sent as an object of osrt?
        //    Rating
        //    is_time_of_month
        //    Image (TBD)
        return log_id;
    }

    private UserDto getUserDto(String auth) throws UsernameNotFoundException {
        String email = jwtUtil.getEmailFromToken(auth);
        UserDto userDto = usersService.getUserDetailsByEmail(email);
        return userDto;
    }

    ;
    //POST  Create Log

    //Delete log

    //Update log

    // Attach image to log (Do this and create log first)
}
