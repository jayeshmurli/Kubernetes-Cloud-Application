package com.neu.backend.util;

import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ValidateUtil {

    public ValidateUtil() {


    }

    public boolean verifyEmail(String email) {

        boolean isValid = false;

        //Email pattern (abc@abc.com , abc@abc.co.in)
        Pattern emailPattern = Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]+$");

        //Match input email string with email pattern and return boolean
        Matcher matcher = emailPattern.matcher(email);
        isValid = matcher.find();
        return isValid;

    }

    public boolean verifyPassword(String password) {

        boolean isValid = false;
        Pattern passwordPattern = Pattern.compile("^(?=.*?[A-Z])(?=(.*[a-z]){1,})(?=(.*[\\d]){1,})(?=(.*[\\W_]){1,})(?!.*\\s).{8,}$");
        Matcher m = passwordPattern.matcher(password);
        isValid = m.find();

        return isValid;
    }

}
