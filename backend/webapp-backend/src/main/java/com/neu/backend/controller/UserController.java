package com.neu.backend.controller;

import com.neu.backend.model.User;
import com.neu.backend.response.ApiResponse;
import com.neu.backend.services.UserService;
import io.micrometer.core.annotation.Timed;
import json.UserCredential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class UserController {


    @Autowired
    UserService userService;


    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Timed(value = "api.v1.user.post")
    @RequestMapping(value="/v1/user", method = RequestMethod.POST)
    public ResponseEntity<Object> registerUser(@RequestBody User user){
        logger.info("adding user");
        return userService.registerUser(user);
    }

    @Timed(value = "api.v1.health")
    @RequestMapping(value="/v1/health", method = RequestMethod.GET)
    public ResponseEntity<Object> checkHealth(){
        logger.info("checking health2");
        return new ResponseEntity<Object>(HttpStatus.OK);
    }

    @Timed(value = "api.v1.user.self.get")
    @RequestMapping(value="/v1/user/self", method= RequestMethod.GET)
    public ResponseEntity<Object> getUser(){
        String message = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ApiResponse apiResponse;
        if (message.equals("Username does not exist") || message.equals("Invalid Credentials")|| message.equals("Username not entered") || message.equals("Password not entered") || message.equals("anonymousUser")) {
            logger.error("Username and invalid credentials");
            apiResponse = new ApiResponse(HttpStatus.UNAUTHORIZED, message, message);
            return new ResponseEntity<Object>(apiResponse, HttpStatus.UNAUTHORIZED);
        }
        logger.info("Get User Self");
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.getUser(email);
    }

    @Timed(value = "api.v1.user.self.put")
    @RequestMapping(value="/v1/user/self", method=RequestMethod.PUT)
    public ResponseEntity<Object> updateUSer(@RequestBody User user){
        String message = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (message.equals("Username does not exist") || message.equals("Invalid Credentials")|| message.equals("Username not entered") || message.equals("Password not entered") || message.equals("anonymousUser")) {
            logger.error("Username and invalid credentials");
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
        logger.info("Update user self");
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.updateUser(user, email);
    }
}
