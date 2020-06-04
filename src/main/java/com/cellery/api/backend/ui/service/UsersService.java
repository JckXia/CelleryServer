package com.cellery.api.backend.ui.service;

import com.cellery.api.backend.shared.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface  UsersService  extends UserDetailsService {
    UserDto createUser(UserDto userDetails);
    UserDto getUserDetailsByEmail(String email);
}
