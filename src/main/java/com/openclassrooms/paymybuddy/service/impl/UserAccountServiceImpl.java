package com.openclassrooms.paymybuddy.service.impl;

import com.openclassrooms.paymybuddy.dto.UserAccountCreationDto;
import com.openclassrooms.paymybuddy.model.UserAccount;
import com.openclassrooms.paymybuddy.repository.UserAccountRepository;
import com.openclassrooms.paymybuddy.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserAccountServiceImpl implements UserAccountService {
    @Autowired
    private UserAccountRepository userAccountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Iterable<UserAccount> getUsers() {
        return userAccountRepository.findAll();
    }

    @Override
    public Optional<UserAccount> getUserById(Integer id) {
        return userAccountRepository.findById(id);
    }

    @Override
    public void saveUserAccount(UserAccountCreationDto userAccountCreationDto) {
        UserAccount userAccount = new UserAccount();
        userAccount.setFirstName(userAccountCreationDto.getFirstName());
        userAccount.setLastName(userAccountCreationDto.getLastName());
        userAccount.setEmail(userAccountCreationDto.getEmail());
        userAccount.setBank(userAccountCreationDto.getBank());
        //encrypt password
        userAccount.setPassword(passwordEncoder.encode(userAccountCreationDto.getPassword()));

        userAccountRepository.save(userAccount);
    }

    @Override
    public Optional<UserAccount> findUserByEmail(String email) {
        return userAccountRepository.findByEmail(email);
    }


}
