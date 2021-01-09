package com.cellery.api.backend.ui.controllers;

import com.cellery.api.backend.shared.UserDto;
import com.cellery.api.backend.shared.Util.MapperUtil;
import com.cellery.api.backend.ui.model.request.CreateUserRequestModel;
import com.cellery.api.backend.ui.model.request.UserVerificationModel;
import com.cellery.api.backend.ui.model.response.CreateUserRespModel;
import com.cellery.api.backend.ui.model.response.UserVerificationRespModel;
import com.cellery.api.backend.ui.service.UsersService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/users")
public class UserController {

    UsersService  usersService;
    MapperUtil mapper;

    @Autowired
    UserController(UsersService usersService, MapperUtil mapper) {
        this.usersService = usersService;
        this.mapper = mapper;
    }

    @GetMapping
    public String statusCheck() {
        return "User API is running!";
    }

    @PostMapping(path= "/create")
    public ResponseEntity<CreateUserRespModel> createUser(@Valid @RequestBody CreateUserRequestModel userDetail) {

        ModelMapper modelMapper = mapper.strictMapper();

        UserDto userDto = modelMapper.map(userDetail,UserDto.class);
        UserDto createUserResp = usersService.createUser(userDto);

        CreateUserRespModel returnValue=new ModelMapper().map(createUserResp,CreateUserRespModel.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(returnValue);
    }

    @PostMapping(path="/verify")
    public ResponseEntity<UserVerificationRespModel> verifyUser(@Valid @RequestBody UserVerificationModel verifyDetail){
        String jwtToken = verifyDetail.getJwtToken();
        UserDto userData =   usersService.getUserDetailsFromToken(jwtToken);
        UserVerificationRespModel verifedUser = new ModelMapper().map(userData,UserVerificationRespModel.class);
        return ResponseEntity.status(HttpStatus.OK).body(verifedUser);
    }
}
