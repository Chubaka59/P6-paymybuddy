package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.dto.ContactDto;
import com.openclassrooms.paymybuddy.dto.TransactionDto;
import com.openclassrooms.paymybuddy.dto.UserAccountCreationDto;
import com.openclassrooms.paymybuddy.model.UserAccount;
import com.openclassrooms.paymybuddy.service.TransactionService;
import com.openclassrooms.paymybuddy.service.UserAccountService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
        when(userAccountService.findUserByEmail(anyString())).thenReturn(Optional.empty());

        UserAccountCreationDto userAccountCreationDto = new UserAccountCreationDto();
        userAccountCreationDto.setLastName("test");
        userAccountCreationDto.setFirstName("test");
        userAccountCreationDto.setEmail("test@test.test");
        userAccountCreationDto.setBank("test");
        userAccountCreationDto.setPassword("test");

        String expectedString = "redirect:/register?success";

        doNothing().when(userAccountService).saveUserAccount(userAccountCreationDto);

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
        when(userAccountService.findUserByEmail(anyString())).thenReturn(Optional.of(new UserAccount()));

        UserAccountCreationDto userAccountCreationDto = new UserAccountCreationDto();
        userAccountCreationDto.setLastName("test");
        userAccountCreationDto.setFirstName("test");
        userAccountCreationDto.setEmail("test@test.test");
        userAccountCreationDto.setBank("test");
        userAccountCreationDto.setPassword("test");

        String expectedString = "register";

        //WHEN we call the registration page
        String actualString = appController.registration(userAccountCreationDto, result, model);

        //THEN the success page is returned
        verify(result, times(1)).rejectValue("email", null, "An account already exist with the Email" + userAccountCreationDto.getEmail());
        assertEquals(expectedString, actualString);
    }

    @Test
    public void showHomePageTest(){
        //GIVEN we expect to get the home page
        String expectedString= "home";

        //WHEN we call this page
        String actualString = appController.showHomePage();

        //THEN we get the correct page
        assertEquals(expectedString, actualString);
    }

    @Test
    public void showTransactionPageTest(){
        //GIVEN we expect to get the home page
        String expectedString= "transaction";
        TransactionDto existingTransaction = new TransactionDto(LocalDate.now(), new BigDecimal(2), "testdesc", "testcred", "testdeb");

        when(transactionService.findTransactionByUser(principal.getName())).thenReturn(List.of(existingTransaction));

        //WHEN we call this page
        String actualString = appController.showTransactionPage(model, principal);

        //THEN we get the correct page
        verify(model, times(1)).addAttribute("transaction", List.of(existingTransaction));
        assertEquals(expectedString, actualString);
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
        UserAccount contactUserAccount = new UserAccount(1, "existingtestuser", "existingtestuser", "existingtestuser", "existingtestuser", "existingtestuser", BigDecimal.valueOf(0), List.of());
        Optional<UserAccount> contact = Optional.of(contactUserAccount);
        UserAccount userAccount = new UserAccount(2, "test", "test", "testmail", "test", "test", BigDecimal.valueOf(0), List.of());
        ContactDto contactDto = new ContactDto("existingtestuser", "existingtestuser");
        when(userAccountService.findUserByEmail(anyString())).thenReturn(contact);
        when(userAccountService.addContact(contact.get(), "testmail")).thenReturn(userAccount);
        when(principal.getName()).thenReturn("testmail");

        String expectedString = "redirect:/contact?success";

        //WHEN we try to add a contact
        String actualString = appController.addContact(contactDto, result, model, principal);

        //THEN the correct string is returned  and the addContact method is called
        verify(userAccountService, times(1)).addContact(contact.get(), "testmail");
        assertEquals(expectedString, actualString);
    }

    @Test
    public void addContactWhenErrorTest(){
        //GIVEN there is an error in the form
        when(result.hasErrors()).thenReturn(true);
        String expectedString = "contact";

        //WHEN we try to add a contact
        String  actualString = appController.addContact(new ContactDto(), result, model, principal);

        //THEN we are redirected on the contact page
        verify(model, times(1)).addAttribute("add_contact", new ContactDto());
        assertEquals(expectedString, actualString);
    }

    @Test
    public void addContactWhenContactIsNotFound(){
        //GIVEN the contact we try to add is not found
        when(userAccountService.findUserByEmail(anyString())).thenReturn(Optional.empty());
        ContactDto contactDto = new ContactDto();

        String expectedString = "contact";

        //WHEN we try to add the contact
        String actualString = appController.addContact(contactDto, result, model, principal);

        //THEN the result is rejected
        verify(result, times(1)).rejectValue("email", null, "There is no account created for the mail : " + contactDto.getEmail());
        assertEquals(expectedString, actualString);
    }
}
