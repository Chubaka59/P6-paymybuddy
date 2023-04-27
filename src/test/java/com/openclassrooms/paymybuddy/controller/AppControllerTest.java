package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.dto.*;
import com.openclassrooms.paymybuddy.exception.UsernameAlreadyExistException;
import com.openclassrooms.paymybuddy.model.Transaction;
import com.openclassrooms.paymybuddy.model.UserAccount;
import com.openclassrooms.paymybuddy.service.TransactionService;
import com.openclassrooms.paymybuddy.service.UserAccountService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AppControllerTest {
    @InjectMocks
    private AppController appController;

    @Mock
    private UserAccountService userAccountService;
    @Mock
    private TransactionService transactionService;
    @Mock
    private Model model;
    @Mock
    private BindingResult result;
    @Mock
    private Principal principal;

    @Test
    public void showLoginPageTest(){
        //GIVEN we expect to get the login page
        String expectedString = "login";

        //WHEN we call this page
        String actualString = appController.showLoginPage();

        //THEN we get the correct page
        assertEquals(expectedString, actualString);
    }

    @Test
    public void showRegistrationPageTest(){
        //GIVEN we expect to get the login page
        String expectedString = "register";

        //WHEN we call this page
        String actualString = appController.showRegistrationPage(model);

        //THEN we get the correct page
        verify(model, times(1)).addAttribute("user_account", new UserAccountCreationDto());
        assertEquals(expectedString, actualString);
    }

    @Test
    public void registrationTest(){
        //GIVEN we will succeed to register a new user
        UserAccountCreationDto userAccountCreationDto = new UserAccountCreationDto();
        userAccountCreationDto.setLastName("test");
        userAccountCreationDto.setFirstName("test");
        userAccountCreationDto.setEmail("test@test.test");
        userAccountCreationDto.setBank("test");
        userAccountCreationDto.setPassword("test");

        String expectedString = "redirect:/register?success";

        when(userAccountService.saveUserAccount(userAccountCreationDto)).thenReturn(new UserAccount());

        //WHEN we call the registration page
        String actualString = appController.registration(userAccountCreationDto, result, model);

        //THEN the success page is returned
        verify(userAccountService, times(1)).saveUserAccount(userAccountCreationDto);
        assertEquals(expectedString, actualString);
    }

    @Test
    public void registrationWhenErrorTest(){
        //GIVEN there is an error in the form
        when(result.hasErrors()).thenReturn(true);

        UserAccountCreationDto userAccountCreationDto = new UserAccountCreationDto();
        userAccountCreationDto.setLastName("test");
        userAccountCreationDto.setFirstName("test");
        userAccountCreationDto.setEmail("test@test.test");
        userAccountCreationDto.setBank("test");
        userAccountCreationDto.setPassword("test");

        String expectedString = "register";

        //WHEN we call the registration page
        String actualString = appController.registration(userAccountCreationDto, result, model);

        //THEN the register page is returned
        verify(model, times(1)).addAttribute("user_account", userAccountCreationDto);
        assertEquals(expectedString, actualString);
    }

    @Test
    public void registrationWhenUserAlreadyExistTest(){
        //GIVEN the account already exist
        UserAccountCreationDto userAccountCreationDto = new UserAccountCreationDto();
        userAccountCreationDto.setLastName("test");
        userAccountCreationDto.setFirstName("test");
        userAccountCreationDto.setEmail("test@test.test");
        userAccountCreationDto.setBank("test");
        userAccountCreationDto.setPassword("test");

        when(userAccountService.saveUserAccount(userAccountCreationDto)).thenThrow(new UsernameAlreadyExistException("test"));

        String expectedString = "register";

        //WHEN we call the registration page
        String actualString = appController.registration(userAccountCreationDto, result, model);

        //THEN the success page is returned
        verify(result, times(1)).rejectValue("email", null, "An account already exist with the email : " + userAccountCreationDto.getEmail());
        assertEquals(expectedString, actualString);
    }

    @Test
    public void showBankPageTest(){
        //GIVEN we expect to get the bank page
        String expectedString = "bank";

        //WHEN we call this page
        String actualString = appController.showBankPage(model, principal, new BankDto());

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
        String actualString = appController.bank(new BankDto(), result, model, principal);

        //THEN the correct String is returned
        assertEquals(expectedString, actualString);
    }

    @Test
    public void bankWhenErrorInTheFormTest(){
        //GIVEN there is an error in the form
        when(result.hasErrors()).thenReturn(true);
        String expectedString = "bank";

        //WHEN we call the method
        String actualString = appController.bank(new BankDto(), result, model, principal);

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

        String actualString = appController.bank(new BankDto(), result, model, principal);

        assertEquals(expectedString, actualString);
        verify(result, times(1)).rejectValue("amount", null, "test");
    }

    @Test
    public void showHomePageTest(){
        //GIVEN we expect to get the home page
        String expectedString= "home";

        //WHEN we call this page
        String actualString = appController.showHomePage(model, principal, new TransferMoneyDto());

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
        String actualString = appController.transferMoney(new TransferMoneyDto(), result, model, principal);

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
        String actualString = appController.transferMoney(new TransferMoneyDto(), result, model, principal);

        //THEN
        assertEquals(expectedString, actualString);
        verify(model, times(2)).addAttribute("transfer_money", new TransferMoneyDto());
    }

    @Test
    public void transferMoneyWhenErrorIsThrewTest(){
        when(principal.getName()).thenReturn("test");
        when(userAccountService.transferMoney(new TransferMoneyDto(), "test")).thenThrow(new UsernameNotFoundException("test"));
        String expectedString = "home";

        String actualString = appController.transferMoney(new TransferMoneyDto(), result, model, principal);

        assertEquals(expectedString, actualString);
        verify(model, times(1)).addAttribute("message_error", "test");
    }

    @Test
    public void showTransactionPageTest(){
        //GIVEN we would get the transaction page with the transactionList
        TransactionDto transactionDto = new TransactionDto(LocalDate.now(), BigDecimal.ONE, "test", "test", "test");
        Page<TransactionDto> transactionDtoPage = new PageImpl(List.of(transactionDto), PageRequest.of(1,5), 1);
        when(principal.getName()).thenReturn("test");
        when(transactionService.findPaginated(PageRequest.of(0,5), "test")).thenReturn(transactionDtoPage);
        String expectedString = "transaction";

        //WHEN we call the method
        String actualString = appController.showTransactionPage(model, principal, 1, 5);

        //THEN we get the correct string and the pagination
        assertEquals(expectedString, actualString);
        verify(model, times(1)).addAttribute("pageNumbers", List.of(1,2));
    }

    @Test
    public void showContactPage(){
        //GIVEN we expect to get the contact page
        String expectedString= "contact";

        //WHEN we call this page
        String actualString = appController.showContactPage(model, principal);

        //THEN we get the correct page
        assertEquals(expectedString, actualString);
    }

    @Test
    public void addContactTest(){
        //GIVEN there is a contact to add
        ContactDto contactDto = new ContactDto("test", "test");
        when(userAccountService.addContact(contactDto, "test")).thenReturn(new UserAccount());
        String expectedString = "redirect:/contact?success";
        when(principal.getName()).thenReturn("test");

        //WHEN we try to add a contact
        String actualString = appController.addContact(contactDto, result, model, principal);

        //THEN the correct string is returned  and the addContact method is called
        assertEquals(expectedString, actualString);
        verify(userAccountService, times(1)).addContact(contactDto, "test");
    }

    @Test
    public void addContactWhenErrorTest(){
        //GIVEN there is an error in the form
        when(result.hasErrors()).thenReturn(true);
        String expectedString = "contact";
        ContactDto contactDto = new ContactDto("test", "test");
        when(principal.getName()).thenReturn("test");
        when(userAccountService.findContactList("test")).thenReturn(List.of(contactDto));

        //WHEN we try to add a contact
        String  actualString = appController.addContact(new ContactDto(), result, model, principal);

        //THEN we are redirected on the contact page
        verify(model, times(1)).addAttribute("add_contact", new ContactDto());
        verify(model, times(1)).addAttribute("contact_list", List.of(contactDto));
        assertEquals(expectedString, actualString);
    }

    @Test
    public void addContactWhenErrorIsThrewTest(){
        //GIVEN an error is threw
        ContactDto contactDto = new ContactDto("test", "test");
        when(userAccountService.addContact(contactDto, "test")).thenThrow(new UsernameNotFoundException("test"));
        String expectedString = "contact";
        when(principal.getName()).thenReturn("test");
        when(userAccountService.findContactList("test")).thenReturn(List.of(contactDto));

        //WHEN the method is called
        String  actualString = appController.addContact(contactDto, result, model, principal);

        //THEN the correct String is returned and with the correct model
        assertEquals(expectedString, actualString);
        verify(model, times(1)).addAttribute("message_error", "test");
        verify(model, times(1)).addAttribute("contact_list", List.of(contactDto));
    }
}
