package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.dto.ContactDto;
import com.openclassrooms.paymybuddy.dto.UserAccountCreationDto;
import com.openclassrooms.paymybuddy.model.UserAccount;
import com.openclassrooms.paymybuddy.repository.UserAccountRepository;
import com.openclassrooms.paymybuddy.service.impl.UserAccountServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserAccountServiceTest {
    @InjectMocks
    private UserAccountServiceImpl userAccountService;

    @Mock
    private UserAccountRepository userAccountRepository;

    @Test
    public void saveUserAccountTest() {
        //GIVEN we would save a user
        UserAccountCreationDto userAccountCreationDto = new UserAccountCreationDto();
        userAccountCreationDto.setFirstName("test");
        userAccountCreationDto.setLastName("test");
        userAccountCreationDto.setEmail("test@test.test");
        userAccountCreationDto.setBank("test");
        userAccountCreationDto.setPassword("test");

        UserAccount expectedUser = new UserAccount(userAccountCreationDto);

        UserAccount userAccount = new UserAccount(userAccountCreationDto);

        when(userAccountRepository.save(any(UserAccount.class))).thenReturn(expectedUser);

        //WHEN the method is called
        userAccountService.saveUserAccount(userAccountCreationDto);

        //THEN the method to save in the repository is called
        verify(userAccountRepository, times(1)).save(any(UserAccount.class));
    }

    @Test
    public void findUserByEmailTest() {
        //GIVEN there is a user to find
        when(userAccountRepository.findByEmail(anyString())).thenReturn(any(Optional.class));

        //WHEN the method is called
        userAccountService.findUserByEmail("test");

        //THEN the method userAccountRepository.findByEmail(email) is called once
        verify(userAccountRepository, times(1)).findByEmail(anyString());
    }

    @Test
    public void findContactListTest() {
        //GIVEN there is a contact list to return
        UserAccount userAccount = new UserAccount();
        UserAccount contact = new UserAccount();
        userAccount.getContactList().add(contact);
        List<ContactDto> expectedContactList = List.of(new ContactDto(contact));
        when(userAccountRepository.findByEmail(anyString())).thenReturn(Optional.of(userAccount));

        //WHEN we get the contact list
        List<ContactDto> contactDtoList = userAccountService.findContactList("test");

        //THEN the contactList is returned
        Assertions.assertEquals(expectedContactList, contactDtoList);
    }

    @Test
    public void findContactListWhenContactIsNotFoundTest() {
        //GIVEN the contact is not found
        when(userAccountRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        //WHEN the method is called THEN an exception is raised
        assertThrows(UsernameNotFoundException.class, () -> userAccountService.findContactList("test"));
    }

    @Test
    public void addContact() {
        UserAccount userAccount = new UserAccount(1, "test", "test", "test", "test", "test", BigDecimal.valueOf(0), new ArrayList<>());
        UserAccount contact = new UserAccount(2, "contact", "contact", "contact", "contact", "contact", BigDecimal.valueOf(0), null);
        when(userAccountRepository.findByEmail(anyString())).thenReturn(Optional.of(contact));
        when(userAccountRepository.findByEmail(userAccount.getEmail())).thenReturn(Optional.of(userAccount));

        userAccount.getContactList().add(contact);

        when(userAccountRepository.save(userAccount)).thenReturn(userAccount);

        userAccountService.addContact(contact, "test");

        verify(userAccountRepository, times(1)).save(any(UserAccount.class));
    }

    @Test
    public void addContactWhenContactIsNotFoundTest(){
        //GIVEN the contact is not found
        when(userAccountRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userAccountService.addContact(new UserAccount(), "test"));
    }

    @Test
    public void addContactWhenUserIsNotFoundTest(){
        //GIVEN the user is not found
        UserAccount userAccount = new UserAccount(1, "test", "test", "test", "test", "test", BigDecimal.valueOf(0), new ArrayList<>());
        UserAccount contact = new UserAccount(2, "contact", "contact", "contact", "contact", "contact", BigDecimal.valueOf(0), null);
        when(userAccountRepository.findByEmail(anyString())).thenReturn(Optional.of(contact));
        when(userAccountRepository.findByEmail(userAccount.getEmail())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userAccountService.addContact(new UserAccount(), "test"));
    }
}
