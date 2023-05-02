package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.dto.UserAccountCreationDto;
import com.openclassrooms.paymybuddy.exception.UsernameAlreadyExistException;
import com.openclassrooms.paymybuddy.model.UserAccount;
import com.openclassrooms.paymybuddy.service.UserAccountService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class RegisterControllerTest {
    @InjectMocks
    private RegisterController registerController;
    @Mock
    private Model model;
    @Mock
    private BindingResult result;
    @Mock
    private UserAccountService userAccountService;

    @Test
    public void showRegistrationPageTest(){
        //GIVEN we expect to get the login page
        String expectedString = "register";

        //WHEN we call this page
        String actualString = registerController.showRegistrationPage(model);

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
        String actualString = registerController.registration(userAccountCreationDto, result, model);

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
        String actualString = registerController.registration(userAccountCreationDto, result, model);

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
        String actualString = registerController.registration(userAccountCreationDto, result, model);

        //THEN the success page is returned
        verify(result, times(1)).rejectValue("email", null, "An account already exist with the email : " + userAccountCreationDto.getEmail());
        assertEquals(expectedString, actualString);
    }
}
