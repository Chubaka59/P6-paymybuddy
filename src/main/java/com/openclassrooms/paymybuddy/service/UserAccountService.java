package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.dto.UserAccountCreationDto;
import com.openclassrooms.paymybuddy.model.UserAccount;

import java.util.Optional;

public interface UserAccountService {
    Iterable<UserAccount> getUsers();

    Optional<UserAccount> getUserById(Integer id);

    void saveUserAccount(UserAccountCreationDto userAccountDto);

    Optional<UserAccount> findUserByEmail(String email);
}
