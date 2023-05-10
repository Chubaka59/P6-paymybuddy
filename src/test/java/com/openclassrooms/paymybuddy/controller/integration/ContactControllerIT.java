package com.openclassrooms.paymybuddy.controller.integration;

import com.openclassrooms.paymybuddy.model.UserAccount;
import com.openclassrooms.paymybuddy.repository.UserAccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ContactControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserAccountRepository userAccountRepository;

    @Test
    @WithUserDetails("joffrey.lefebvre@gmail.com")
    public void getContactPageTest() throws Exception {
        mockMvc.perform(get("/contact"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("contact"))
                .andExpect(model().attributeExists("contact_list"))
                .andExpect(model().attributeExists("add_contact"));
    }

    @Test
    @WithUserDetails("spiderman@marvel.com")
    public void postContactTest() throws Exception {
        mockMvc.perform(post("/contact")
                        .param("email", "joffrey.lefebvre@gmail.com")
                        .with(csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/contact?success"));

        UserAccount contact = userAccountRepository.findByEmail("spiderman@marvel.com").get().getContactList().get(0);
        Assertions.assertEquals("joffrey.lefebvre@gmail.com", contact.getEmail());
    }

    @Test
    @WithUserDetails("spiderman@marvel.com")
    public void postContactWhenContactIsNotFoundTest() throws Exception {
        mockMvc.perform(post("/contact")
                        .param("email", "joffrey.lefebvre@gmail.co")
                        .with(csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("contact"))
                .andExpect(model().attributeExists("message_error"));

        int listSize = userAccountRepository.findByEmail("spiderman@marvel.com").get().getContactList().size();
        Assertions.assertEquals(0, listSize);
    }

    @Test
    @WithUserDetails("spiderman@marvel.com")
    public void postContactWhenErrorInTheFormTest() throws Exception {
        mockMvc.perform(post("/contact")
                        .param("email", "")
                        .with(csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("contact"));

        int listSize = userAccountRepository.findByEmail("spiderman@marvel.com").get().getContactList().size();
        Assertions.assertEquals(0, listSize);
    }
}
