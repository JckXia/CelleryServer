package com.cellery.api.backend.ui.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/log")
public class LogController {
   @Autowired
   LogController(){

   }
   @GetMapping
   public String testReturn(){
       return "testRest";
   }
}
