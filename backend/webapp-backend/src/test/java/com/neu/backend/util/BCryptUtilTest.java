package com.neu.backend.util;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCrypt;

public class BCryptUtilTest {

    BCryptUtil bcryptUtil = new BCryptUtil();

    @Test
    public void generateEncryptedPasswordTest() {
        String actualEncryptedPass = this.bcryptUtil.generateEncryptedPassword("abc@123");
        Assert.assertTrue(BCrypt.checkpw("abc@123", actualEncryptedPass));
    }
}
