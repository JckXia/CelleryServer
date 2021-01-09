package com.cellery.api.backend.ui.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping(path = "/")
public class GeneralController {

    private Environment env;


    @Autowired
    GeneralController(Environment env) {
        this.env = env;
    }

    @GetMapping
    public ResponseEntity<String> healthCheck(){
        return ResponseEntity.status(HttpStatus.OK).body("System up and running");
    }

}
