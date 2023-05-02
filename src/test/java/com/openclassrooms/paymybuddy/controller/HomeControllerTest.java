package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.dto.TransferMoneyDto;
import com.openclassrooms.paymybuddy.model.Transaction;
import com.openclassrooms.paymybuddy.service.UserAccountService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.security.Principal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@SpringBootTest
public class HomeControllerTest {
    @InjectMocks
    private HomeController homeController;
    @Mock
    private Model model;
    @Mock
    private BindingResult result;
    @Mock
    private Principal principal;
    @Mock
    private UserAccountService userAccountService;

    @Test
    public void showHomePageTest(){
        //GIVEN we expect to get the home page
        String expectedString= "home";

        //WHEN we call this page
        String actualString = homeController.showHomePage(model, principal, new TransferMoneyDto());

        //THEN we get the correct page
        assertEquals(expectedString, actualString);
    }

    @Test
    public void transferMoneyTest(){
        //GIVEN we would succeed to transfer money
        when(principal.getName()).thenReturn("test");
        when(userAccountService.transferMoney(new TransferMoneyDto(), "test")).thenReturn(new Transaction());
        String expectedString = "redirect:/home?success";

        //WHEn we call the method
        String actualString = homeController.transferMoney(new TransferMoneyDto(), result, model, principal);

        //THEN the correct method is called and the correct string is returned
        assertEquals(expectedString, actualString);
        verify(userAccountService, times(1)).transferMoney(new TransferMoneyDto(), "test");
    }

    @Test
    public void transferMoneyWhenFormHasErrorTest(){
        //GIVEN the form will return an error
        when(result.hasErrors()).thenReturn(true);
        String expectedString = "home";

        //WHEN we call the method
        String actualString = homeController.transferMoney(new TransferMoneyDto(), result, model, principal);

        //THEN
        assertEquals(expectedString, actualString);
        verify(model, times(2)).addAttribute("transfer_money", new TransferMoneyDto());
    }

    @Test
    public void transferMoneyWhenErrorIsThrewTest(){
        when(principal.getName()).thenReturn("test");
        when(userAccountService.transferMoney(new TransferMoneyDto(), "test")).thenThrow(new UsernameNotFoundException("test"));
        String expectedString = "home";

        String actualString = homeController.transferMoney(new TransferMoneyDto(), result, model, principal);

        assertEquals(expectedString, actualString);
        verify(model, times(1)).addAttribute("message_error", "test");
    }
}
