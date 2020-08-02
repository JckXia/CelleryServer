package com.cellery.api.backend.ui.controllers;

import com.cellery.api.backend.shared.LogDto;
import com.cellery.api.backend.shared.UserDto;
import com.cellery.api.backend.shared.Util.JwtUtil;
import com.cellery.api.backend.shared.Util.MapperUtil;
import com.cellery.api.backend.ui.model.request.CreateLogRequestModel;
import com.cellery.api.backend.ui.model.request.UpdateLogRequestModel;
import com.cellery.api.backend.ui.model.response.CreateLogRespModel;
import com.cellery.api.backend.ui.model.response.UpdateLogRespModel;
import com.cellery.api.backend.ui.service.LogsService;
import com.cellery.api.backend.ui.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


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
    public ResponseEntity<List<LogDto>> fetchLogRange(@RequestParam("startDateStamp") Long startDate,
                                                      @RequestParam("endDateStamp") Long endDate,
                                                      @RequestHeader(value = "${authentication.authorization}") String auth) {
        try {
            String authToken = auth.replace(env.getProperty("authentication.bearer"), "");
            UserDto userDto = getUserDto(authToken);
            if (!jwtUtil.validateToken(authToken, userDto)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
            List<LogDto> logDtoList = logsService.getLogEntityBetweenTimeStamps(userDto.getId(),startDate, endDate);
            return ResponseEntity.status(HttpStatus.OK).body(logDtoList);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<CreateLogRespModel> createLog(@RequestHeader(value = "${authentication.authorization}") String auth,
                                                        @Valid @RequestBody CreateLogRequestModel logCreation) {

        try {
            String authToken = auth.replace(env.getProperty("authentication.bearer"), "");
            UserDto userDto = getUserDto(authToken);
            if (!jwtUtil.validateToken(authToken, userDto)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
            LogDto logCreationObject = logsService.createLog(logCreation, userDto.getEmail());
            // We need to get the userDto (current user, and create it using a serviceImpl)
            CreateLogRespModel returnModel = modelMapper.strictMapper().map(logCreationObject, CreateLogRespModel.class);
            return ResponseEntity.status(HttpStatus.CREATED).body(returnModel);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping(path = "/{log_id}", consumes = "application/json", produces =
            "application/json")
    public ResponseEntity<UpdateLogRespModel> updateLog(@PathVariable String log_id,
                                                        @Valid @RequestBody UpdateLogRequestModel logUpdates,
                                                        @RequestHeader(value = "${authentication.authorization}") String auth) {

        try {
            String authToken = auth.replace(env.getProperty("authentication.bearer"), "");
            UserDto userDto = getUserDto(authToken);
            if (!jwtUtil.validateToken(authToken, userDto)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            if (!logsService.logBelongsToUser(userDto.getEmail(), log_id) && !logsService.logCanBeEdited(logUpdates, log_id)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            LogDto logUpdateObject = logsService.updateLogEntity(logUpdates, log_id);
            UpdateLogRespModel returnLogUpdateModel = modelMapper.strictMapper().map(logUpdateObject, UpdateLogRespModel.class);
            return ResponseEntity.status(HttpStatus.OK).body(returnLogUpdateModel);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
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
