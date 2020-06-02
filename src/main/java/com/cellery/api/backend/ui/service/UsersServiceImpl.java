package com.cellery.api.backend.ui.service;

import com.cellery.api.backend.shared.UserDto;
import com.cellery.api.backend.ui.data.UserEntity;
import com.cellery.api.backend.ui.data.UsersRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UsersServiceImpl implements UsersService {

    BCryptPasswordEncoder bCryptPasswordEncoder;
//    UsersRepository usersRepository;


    @Autowired
    public UsersServiceImpl(  BCryptPasswordEncoder bcryptPasswordEncoder) {
        this.bCryptPasswordEncoder=bcryptPasswordEncoder;
//        this.usersRepository=usersRepository;
    }

    @Override
    public UserDto createUser(UserDto userDetails) {
        userDetails.setUserId(UUID.randomUUID().toString());
        userDetails.setEncryptedPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = modelMapper.map(userDetails,UserEntity.class);
        System.out.println(userEntity);
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return null;
    }
}
