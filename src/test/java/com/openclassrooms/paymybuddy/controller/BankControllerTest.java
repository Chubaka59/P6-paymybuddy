package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.dto.BankDto;
import com.openclassrooms.paymybuddy.model.UserAccount;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@SpringBootTest
public class BankControllerTest {
    @InjectMocks
    private BankController bankController;
    @Mock
    private Model model;
    @Mock
    private BindingResult result;
    @Mock
    private Principal principal;
    @Mock
    private UserAccountService userAccountService;

    @Test
    public void showBankPageTest(){
        //GIVEN we expect to get the bank page
        String expectedString = "bank";

        //WHEN we call this page
        String actualString = bankController.showBankPage(model, principal, new BankDto());

        //THEN we get the correct page
        assertEquals(expectedString, actualString);
    }

    @Test
    public void bankTest(){
        //GIVEN we will success to transfer money from the bank
        when(result.hasErrors()).thenReturn(false);
        when(userAccountService.bankTransfer(any(BankDto.class), anyString())).thenReturn(new UserAccount());
        String expectedString = "redirect:/bank?success";

        //WHEN we call the method
        String actualString = bankController.bank(new BankDto(), result, model, principal);

        //THEN the correct String is returned
        assertEquals(expectedString, actualString);
    }

    @Test
    public void bankWhenErrorInTheFormTest(){
        //GIVEN there is an error in the form
        when(result.hasErrors()).thenReturn(true);
        String expectedString = "bank";

        //WHEN we call the method
        String actualString = bankController.bank(new BankDto(), result, model, principal);

        //THEN the correct model and string is called
        assertEquals(expectedString, actualString);
        verify(model, times(1)).addAttribute("transfer_amount", new BankDto());
        verify(model, times(1)).addAttribute("balance", null);
    }

    @Test
    public void bankWhenErrorIsThrewTest(){
        when(result.hasErrors()).thenReturn(false);
        when(principal.getName()).thenReturn("test");
        when(userAccountService.bankTransfer(new BankDto(), "test")).thenThrow(new UsernameNotFoundException("test"));
        String expectedString = "bank";

        String actualString = bankController.bank(new BankDto(), result, model, principal);

        assertEquals(expectedString, actualString);
        verify(result, times(1)).rejectValue("amount", null, "test");
    }
}
