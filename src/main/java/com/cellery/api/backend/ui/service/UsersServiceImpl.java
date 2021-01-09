package com.cellery.api.backend.ui.service;

import com.cellery.api.backend.shared.UserDto;
import com.cellery.api.backend.shared.Util.JwtUtil;
import com.cellery.api.backend.shared.Util.MapperUtil;
import com.cellery.api.backend.ui.data.UserEntity;
import com.cellery.api.backend.ui.data.UsersRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class UsersServiceImpl implements UsersService {

    BCryptPasswordEncoder bCryptPasswordEncoder;
    UsersRepository usersRepository;
    MapperUtil mapper;
    JwtUtil jwtUtil;

    @Autowired
    public UsersServiceImpl(BCryptPasswordEncoder bcryptPasswordEncoder, UsersRepository usersRepository, MapperUtil mapper, JwtUtil jwtUtil) {
        this.bCryptPasswordEncoder = bcryptPasswordEncoder;
        this.usersRepository = usersRepository;
        this.mapper = mapper;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public UserDto createUser(UserDto userDetails) {
        userDetails.setUserId(UUID.randomUUID().toString());
        userDetails.setEncryptedPassword(bCryptPasswordEncoder.encode(userDetails.getPassword()));

        ModelMapper modelMapper = mapper.strictMapper();
        UserEntity userEntity = modelMapper.map(userDetails, UserEntity.class);

        // make sure routines is empty
        userEntity.setRoutines(new ArrayList<>());

        usersRepository.save(userEntity);

        UserDto returnDto = modelMapper.map(userEntity, UserDto.class);
        return returnDto;
    }

    @Transactional
    @Override
    public UserDto getUserDetailsByEmail(String email) throws UsernameNotFoundException {
        UserEntity userEntityDbObject = usersRepository.findByEmail(email);
        if (userEntityDbObject == null) {
            throw new UsernameNotFoundException("User " + email + " does not exist!");
        }
        return new ModelMapper().map(userEntityDbObject, UserDto.class);
    }

    @Override
    public UserDto getUserDetailsFromToken(String jwtToken) {
        String userEmail = jwtUtil.getEmailFromToken(jwtToken);
        UserEntity userEntityObject = usersRepository.findByEmail(userEmail);
        if(userEntityObject == null){
            throw new UsernameNotFoundException("User "+userEmail+" does not exist!");
        }
        return new ModelMapper().map(userEntityObject,UserDto.class);
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
