package com.cellery.api.backend.ui.service;

import com.cellery.api.backend.shared.UserDto;
import com.cellery.api.backend.ui.data.UserEntity;
import com.cellery.api.backend.ui.data.UsersRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class UsersServiceImpl implements UsersService {

    BCryptPasswordEncoder bCryptPasswordEncoder;
    UsersRepository usersRepository;


    @Autowired
    public UsersServiceImpl(BCryptPasswordEncoder bcryptPasswordEncoder, UsersRepository usersRepository) {
        this.bCryptPasswordEncoder = bcryptPasswordEncoder;
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDto createUser(UserDto userDetails) {
        userDetails.setUserId(UUID.randomUUID().toString());
        userDetails.setEncryptedPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = modelMapper.map(userDetails, UserEntity.class);
        usersRepository.save(userEntity);

        UserDto returnDto = modelMapper.map(userEntity, UserDto.class);
        return returnDto;
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {
        UserEntity userEntityDbObject = usersRepository.findByEmail(email);
        if (userEntityDbObject == null) {
            throw new UsernameNotFoundException("User " + email + " does not exist!");
        }
        return new ModelMapper().map(userEntityDbObject, UserDto.class);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = usersRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Email " + email + " does not exist in DB");
        }
        return new User(user.getEmail(), user.getEncryptedPassword(), true, true, true, true, new ArrayList<>());
    }
}
