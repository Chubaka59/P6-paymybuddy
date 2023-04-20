package com.openclassrooms.paymybuddy.service.impl;

import com.openclassrooms.paymybuddy.model.UserAccount;
import com.openclassrooms.paymybuddy.repository.UserAccountRepository;
import com.openclassrooms.paymybuddy.service.PrincipalUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PrincipalUserImpl implements PrincipalUser {

    private final UserAccountRepository userAccountRepository;

    @Override
    public UserAccount getConnectedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userDetails = (User) authentication.getPrincipal();
        return userAccountRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email : " + userDetails.getUsername()));
    }
}
