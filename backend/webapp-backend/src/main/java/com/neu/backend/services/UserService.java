package com.neu.backend.services;

import com.neu.backend.controller.UserController;
import com.neu.backend.dao.UserDao;
import com.neu.backend.model.User;
import com.neu.backend.response.ApiResponse;
import com.neu.backend.util.BCryptUtil;
import com.neu.backend.util.ValidateUtil;
import json.UserCredential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserService {


    @Autowired
    private BCryptUtil bCrptUtil;

    @Autowired
    private ValidateUtil validUtil;

    @Autowired
    UserController userController;

    @Autowired
    UserDao userDao;

    @Autowired
    LoginService loginService;


    public ResponseEntity<Object> registerUser(User user) {


        if (!validUtil.verifyEmail(user.getEmail_address())) {

            ApiResponse apiError = new ApiResponse(HttpStatus.BAD_REQUEST,
                    "Invalid syntax for this request was provided.", "Not an valid email address");
            return new ResponseEntity<Object>(apiError, HttpStatus.BAD_REQUEST);
        } else if (loginService.checkIfUserExists(user.getEmail_address())) {
            ApiResponse apiError = new ApiResponse(HttpStatus.BAD_REQUEST,
                    "Invalid syntax for this request was provided.", "User with same email already exists");
            return new ResponseEntity<Object>(apiError, HttpStatus.BAD_REQUEST);
        } else if (!validUtil.verifyPassword(user.getPassword())) {
            ApiResponse apiError = new ApiResponse(HttpStatus.BAD_REQUEST,
                    "Invalid syntax for this request was provided.",
                    "Password must contain more than 1 character and no whitespaces");
            return new ResponseEntity<Object>(apiError, HttpStatus.BAD_REQUEST);
        } else {
            user.setPassword(this.bCrptUtil.generateEncryptedPassword(user.getPassword()));
            userDao.saveUser(user);
            User user1 = userDao.getUser(user.getEmail_address());
            //ApiResponse apiresponse = new ApiResponse(HttpStatus.OK, user1, "NA");
            return new ResponseEntity<Object>(user1, HttpStatus.CREATED);

        }
    }

    public ResponseEntity<Object> getUser(String email){
        if (!validUtil.verifyEmail(email)) {

            ApiResponse apiError = new ApiResponse(HttpStatus.BAD_REQUEST,
                    "Invalid syntax for this request was provided.", "Not an valid email address");
            return new ResponseEntity<Object>(apiError, HttpStatus.BAD_REQUEST);
        } else if (!loginService.checkIfUserExists(email)) {
            ApiResponse apiError = new ApiResponse(HttpStatus.BAD_REQUEST,
                    "Invalid syntax for this request was provided.", "User with same email does not exist");
            return new ResponseEntity<Object>(apiError, HttpStatus.BAD_REQUEST);
        } else {

            User user1 = userDao.getUser(email);
            //ApiResponse apiresponse = new ApiResponse(HttpStatus.OK, user1, "NA");
            return new ResponseEntity<Object>(user1, HttpStatus.OK);

        }

    }

    public ResponseEntity<Object> updateUser(User user, String email) {

        if(user.getPassword()==null || user.getFirst_name()==null || user.getLast_name()==null || user.getEmail_address()==null){
            ApiResponse apiError = new ApiResponse(HttpStatus.BAD_REQUEST,
                    "Invalid syntax for this request was provided.", "Not a valid request");
            return new ResponseEntity<Object>(apiError, HttpStatus.BAD_REQUEST);
        }
        if (!validUtil.verifyEmail(email)) {

            ApiResponse apiError = new ApiResponse(HttpStatus.BAD_REQUEST,
                    "Invalid syntax for this request was provided.", "Not an valid email address");
            return new ResponseEntity<Object>(apiError, HttpStatus.BAD_REQUEST);
        } else if (!loginService.checkIfUserExists(email)) {
            ApiResponse apiError = new ApiResponse(HttpStatus.BAD_REQUEST,
                    "Invalid syntax for this request was provided.", "User with same email does not exist");
            return new ResponseEntity<Object>(apiError, HttpStatus.BAD_REQUEST);
        } else if (!email.equals(user.getEmail_address())) {
            ApiResponse apiError = new ApiResponse(HttpStatus.BAD_REQUEST,
                    "Invalid syntax for this request was provided.", "Not a valid email address");
            return new ResponseEntity<Object>(apiError, HttpStatus.BAD_REQUEST);
        } else if (user.getId()!=null || user.getAccount_created()!=null || user.getAccount_updated()!=null){
            ApiResponse apiError = new ApiResponse(HttpStatus.BAD_REQUEST,
                    "Invalid syntax for this request was provided.", "Invalid Request");
            return new ResponseEntity<Object>(apiError, HttpStatus.BAD_REQUEST);
        }
        else {
            User userFromDb = userDao.getUser(email);
            String Id = userFromDb.getId();
            //System.out.println("password from request: "+user.getPassword());
            user.setPassword(this.bCrptUtil.generateEncryptedPassword(user.getPassword()));
            User user1 = userDao.updateUser(user, Id);
            //ApiResponse apiresponse = new ApiResponse(HttpStatus.OK, user1, "NA");
            return new ResponseEntity<Object>(null, HttpStatus.NO_CONTENT);
        }
    }
}
