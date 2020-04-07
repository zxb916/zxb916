package com.example.demo.util;

import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * Created by Administrator on 2017/4/12.
 */
public class UUIDGenerator {

    public static String generateUUID() throws NoSuchAlgorithmException {
        String tempUUID = UUID.randomUUID().toString();
        String uuid = tempUUID.replaceAll("-","");
        return uuid ;
    }

    public static void main(String[] args){
        try {
            String test = UUIDGenerator.generateUUID();
            System.out.print(test);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

    public static String getUUID()  {
        String tempUUID = UUID.randomUUID().toString();
        String uuid = tempUUID.replaceAll("-","");
        return uuid;
    }
}
