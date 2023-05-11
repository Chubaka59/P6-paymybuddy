package com.openclassrooms.paymybuddy.securingweb;

import com.openclassrooms.paymybuddy.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Set;

@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserAccountRepository userAccountRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userAccountRepository.findByEmail(email)
                .map(user -> new User(user.getEmail(), user.getPassword(), Set.of()))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email : " + email));
    }
}
