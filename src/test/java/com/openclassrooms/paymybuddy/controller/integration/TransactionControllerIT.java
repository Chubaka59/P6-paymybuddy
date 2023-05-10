package com.openclassrooms.paymybuddy.controller.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithUserDetails("joffrey.lefebvre@gmail.com")
    public void getTransactionPageTest() throws Exception {
        mockMvc.perform(get("/transaction"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(view().name("transaction"))
                .andExpect(model().attributeExists("pageNumbers"))
                .andExpect(model().attributeExists("transactionDtoPage"));
    }
}
