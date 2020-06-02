package com.cellery.api.backend.ui.controllers;


import com.cellery.api.backend.shared.UserDto;
import com.cellery.api.backend.ui.data.UserEntity;
import com.cellery.api.backend.ui.data.UsersRepository;
import com.cellery.api.backend.ui.model.request.CreateUserRequestModel;
import com.cellery.api.backend.ui.service.UsersService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;


@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UsersService  usersService;

    @GetMapping
    public String statusCheck() {
        return "User API is running!";
    }

    @PostMapping(path= "/create")
    public String createUser(@Valid @RequestBody CreateUserRequestModel userDetail) {

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UserDto userDto = modelMapper.map(userDetail,UserDto.class);
        UserDto createUserResp = usersService.createUser(userDto);
        return "Creation API is working!";
    }

}
