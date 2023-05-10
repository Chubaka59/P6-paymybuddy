package com.openclassrooms.paymybuddy.controller.integration;

import com.openclassrooms.paymybuddy.dto.UserAccountCreationDto;
import com.openclassrooms.paymybuddy.model.UserAccount;
import com.openclassrooms.paymybuddy.repository.UserAccountRepository;
import com.openclassrooms.paymybuddy.securingweb.CustomUserDetailsService;
import com.openclassrooms.paymybuddy.securingweb.SecurityConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class RegisterControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserAccountRepository userAccountRepository;

    @Test
    public void getRegistrationPageTest() throws Exception {
        mockMvc.perform(get("/register"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("user_account"));
    }

    @Test
    public void postRegistrationPageTest() throws Exception {
        mockMvc.perform(post("/register/save")
                        .param("firstName", "firstname")
                        .param("lastName", "lastname")
                        .param("email", "test@test.test")
                        .param("password", "password")
                        .param("bank", "bank")
                        .with(csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/register?success"));

        Optional<UserAccount> userAccount = userAccountRepository.findByEmail("test@test.test");
        Assertions.assertTrue(userAccount.isPresent());
    }

    @Test
    public void postRegistrationWhenErrorInTheFormTest() throws Exception {
        mockMvc.perform(post("/register/save")
                        .param("firstName", "firstname")
                        .param("lastName", "lastname")
                        .param("email", "test@test.test")
                        .param("password", "")
                        .param("bank", "bank")
                        .with(csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("register"));

        Optional<UserAccount> userAccount = userAccountRepository.findByEmail("test@test.test");
        Assertions.assertTrue(userAccount.isEmpty());
    }

    @Test
    public void postRegistrationWhenUserAlreadyExistTest() throws Exception {
        mockMvc.perform(post("/register/save")
                        .param("firstName", "firstname")
                        .param("lastName", "lastname")
                        .param("email", "joffrey.lefebvre@gmail.com")
                        .param("password", "password")
                        .param("bank", "bank")
                        .with(csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("register"));

        Optional<UserAccount> userAccount = userAccountRepository.findByEmail("test@test.test");
        Assertions.assertTrue(userAccount.isEmpty());
    }
}
