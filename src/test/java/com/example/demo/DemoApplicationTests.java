package com.example.demo;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

    @Test
    void contextLoads() {

        getUserName();
    }


    private void getUserName() {

        String lam = "初次相识Lambda";
        new Thread(() -> System.out.println(lam)).start();
    }


}
