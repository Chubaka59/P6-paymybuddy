package com.openclassrooms.paymybuddy.controller.integration;

import com.openclassrooms.paymybuddy.repository.UserAccountRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.math.BigDecimal;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BankControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserAccountRepository userAccountRepository;

    @Test
    @WithUserDetails("joffrey.lefebvre@gmail.com")
    public void getBankTest() throws Exception{
        mockMvc.perform(get("/bank"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("bank"))
                .andExpect(model().attributeExists("transfer_amount"))
                .andExpect(model().attributeExists("balance"));
    }

    @Test
    @WithUserDetails("joffrey.lefebvre@gmail.com")
    public void postBankTest() throws Exception {
        mockMvc.perform(post("/bank")
                        .param("amount", "10")
                        .with(csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/bank?success"));

        BigDecimal actualBalance = userAccountRepository.findByEmail("joffrey.lefebvre@gmail.com").get().getBalance();
        Assertions.assertEquals(BigDecimal.valueOf(210).setScale(2), actualBalance);
    }

    @Test
    @WithUserDetails("joffrey.lefebvre@gmail.com")
    public void postBankWhenErrorInTheFormTest() throws Exception {
        mockMvc.perform(post("/bank")
                        .param("amount", "")
                        .with(csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("bank"))
                .andExpect(model().attributeExists("transfer_amount"))
                .andExpect(model().attributeExists("balance"));

        BigDecimal actualBalance = userAccountRepository.findByEmail("joffrey.lefebvre@gmail.com").get().getBalance();
        Assertions.assertEquals(BigDecimal.valueOf(200).setScale(2), actualBalance);
    }

    @Test
    @WithUserDetails("joffrey.lefebvre@gmail.com")
    public void postBankWhenExceptionIsThrownTest() throws Exception {
        mockMvc.perform(post("/bank")
                        .param("amount", "")
                        .with(csrf()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("bank"))
                .andExpect(model().attributeExists("transfer_amount"))
                .andExpect(model().attributeExists("balance"));

        BigDecimal actualBalance = userAccountRepository.findByEmail("joffrey.lefebvre@gmail.com").get().getBalance();
        Assertions.assertEquals(BigDecimal.valueOf(200).setScale(2), actualBalance);
    }
}
