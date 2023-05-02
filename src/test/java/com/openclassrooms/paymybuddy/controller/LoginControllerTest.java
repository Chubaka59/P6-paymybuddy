package com.openclassrooms.paymybuddy.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class LoginControllerTest {
    @Autowired
    private LoginController loginController;

    @Test
    public void showLoginPageTest(){
        //GIVEN we expect to get the login page
        String expectedString = "login";

        //WHEN we call this page
        String actualString = loginController.showLoginPage();

        //THEN we get the correct page
        assertEquals(expectedString, actualString);
    }
}
