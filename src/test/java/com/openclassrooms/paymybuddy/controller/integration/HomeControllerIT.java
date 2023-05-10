package com.openclassrooms.paymybuddy.controller.integration;

import com.openclassrooms.paymybuddy.repository.UserAccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.test.context.transaction.TestTransaction;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class HomeControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserAccountRepository userAccountRepository;

    @Test
    @WithUserDetails("joffrey.lefebvre@gmail.com")
    void getHomeTest() throws Exception{
        mockMvc.perform(get("/home"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("balance"))
                .andExpect(model().attributeExists("contact_list"))
                .andExpect(model().attributeExists("transfer_money"));

//                .andExpect(status().is3xxRedirection())
//                .andExpect(header().string("Location", "/home"))
    }

    @Test
    @WithUserDetails("joffrey.lefebvre@gmail.com")
    public void postHomeTest() throws Exception {
         mockMvc.perform(post("/home")
                        .param("contactEmail", "spiderman@marvel.com")
                        .param("amount", "100")
                        .param("description", "testdescr")
                        .with(csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/home?success"));

        BigDecimal actualDebtorBalance = userAccountRepository.findByEmail("spiderman@marvel.com").get().getBalance();
        BigDecimal actualCreditorBalance = userAccountRepository.findByEmail("joffrey.lefebvre@gmail.com").get().getBalance().setScale(2);
        assertEquals(BigDecimal.valueOf(100).setScale(2), actualDebtorBalance);
        assertEquals(BigDecimal.valueOf(99.50).setScale(2), actualCreditorBalance);
    }

    @Test
    @WithUserDetails("joffrey.lefebvre@gmail.com")
    public void postHomeWhenErrorInTheFormTest() throws Exception {
        mockMvc.perform(post("/home")
                        .param("contactEmail", "spiderman@marvel.com")
                        .param("description", "testdescr")
                        .with(csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("transfer_money"));

        BigDecimal actualDebtorBalance = userAccountRepository.findByEmail("spiderman@marvel.com").get().getBalance();
        BigDecimal actualCreditorBalance = userAccountRepository.findByEmail("joffrey.lefebvre@gmail.com").get().getBalance();
        assertEquals(BigDecimal.valueOf(0).setScale(2), actualDebtorBalance);
        assertEquals(BigDecimal.valueOf(200).setScale(2), actualCreditorBalance);
    }

    @Test
    @WithUserDetails("joffrey.lefebvre@gmail.com")
    public void postHomeWhenExceptionIsThrownTest() throws Exception {
        mockMvc.perform(post("/home")
                        .param("contactEmail", "test@test.test")
                        .param("amount", "100")
                        .param("description", "testdescr")
                        .with(csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("message_error"));

        BigDecimal actualCreditorBalance = userAccountRepository.findByEmail("joffrey.lefebvre@gmail.com").get().getBalance();
        assertEquals(BigDecimal.valueOf(200).setScale(2), actualCreditorBalance);
    }
}
