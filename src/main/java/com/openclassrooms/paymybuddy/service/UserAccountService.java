package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.dto.ContactDto;
import com.openclassrooms.paymybuddy.dto.UserAccountCreationDto;
import com.openclassrooms.paymybuddy.model.UserAccount;

import java.util.List;
import java.util.Optional;

public interface UserAccountService {
    Iterable<UserAccount> getUsers();

    Optional<UserAccount> getUserById(Integer id);

    void saveUserAccount(UserAccountCreationDto userAccountDto);

    Optional<UserAccount> findUserByEmail(String email);

    List<ContactDto> findContactList(String email);

    UserAccount addContact(UserAccount userAccount, String email);
}
