package com.cellery.api.backend.ui.service;

import com.cellery.api.backend.ui.data.RoutinesRepository;
import com.cellery.api.backend.ui.data.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoutinesService {
    private RoutinesRepository routinesRepository;
    private UsersRepository usersRepository;

    @Autowired
    public RoutinesService(RoutinesRepository routinesRepository, UsersRepository usersRepository) {
        this.routinesRepository = routinesRepository;
        this.usersRepository = usersRepository;
    }

    /* TODO:
        Get routines
        Create routine
        ?? remove products from routine?
    */
}
