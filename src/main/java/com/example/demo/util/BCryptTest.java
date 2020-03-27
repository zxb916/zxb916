package com.example.demo.util;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class BCryptTest {


    public static void main(String[] args) {
        //前段加密 const bcrypt = require('bcryptjs') //加密
        //await bcrypt.genSalt(12, (err, salt)
        // Hash a password for the first time
        String password = "testpassword";
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
        System.out.println(hashed);
        // gensalt's log_rounds parameter determines the complexity
        // the work factor is 2**log_rounds, and the default is 10
        String hashed2 = BCrypt.hashpw(password, BCrypt.gensalt(12));

        // Check that an unencrypted password matches one that has
        // previously been hashed
        String candidate = "testpassword";
        //String candidate = "wrongtestpassword";
        if (BCrypt.checkpw(candidate, hashed))
            System.out.println("It matches");
        else
            System.out.println("It does not match");
    }

}

