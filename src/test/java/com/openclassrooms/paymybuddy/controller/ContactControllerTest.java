package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.dto.ContactDto;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@SpringBootTest
public class ContactControllerTest {
    @InjectMocks
    private ContactController contactController;
    @Mock
    private UserAccountService userAccountService;
    @Mock
    private Model model;
    @Mock
    private BindingResult result;
    @Mock
    private Principal principal;

    @Test
    public void showContactPage(){
        //GIVEN we expect to get the contact page
        String expectedString= "contact";

        //WHEN we call this page
        String actualString = contactController.showContactPage(model, principal);

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
        String actualString = contactController.addContact(contactDto, result, model, principal);

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
        String  actualString = contactController.addContact(new ContactDto(), result, model, principal);

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
        String  actualString = contactController.addContact(contactDto, result, model, principal);

        //THEN the correct String is returned and with the correct model
        assertEquals(expectedString, actualString);
        verify(model, times(1)).addAttribute("message_error", "test");
        verify(model, times(1)).addAttribute("contact_list", List.of(contactDto));
    }
}
