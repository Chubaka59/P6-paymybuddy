package com.openclassrooms.paymybuddy.controller;

import com.openclassrooms.paymybuddy.dto.TransactionDto;
import com.openclassrooms.paymybuddy.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TransactionControllerTest {
    @InjectMocks
    private TransactionController transactionController;
    @Mock
    private TransactionService transactionService;
    @Mock
    private Model model;
    @Mock
    private Principal principal;

    @Test
    public void showTransactionPageTest(){
        //GIVEN we would get the transaction page with the transactionList
        TransactionDto transactionDto = new TransactionDto(LocalDate.now(), BigDecimal.ONE, "test", "test", "test");
        Page<TransactionDto> transactionDtoPage = new PageImpl(List.of(transactionDto), PageRequest.of(1,5), 1);
        when(principal.getName()).thenReturn("test");
        when(transactionService.findPaginated(PageRequest.of(0,5), "test")).thenReturn(transactionDtoPage);
        String expectedString = "transaction";

        //WHEN we call the method
        String actualString = transactionController.showTransactionPage(model, principal, 1, 5);

        //THEN we get the correct string and the pagination
        assertEquals(expectedString, actualString);
        verify(model, times(1)).addAttribute("pageNumbers", List.of(1,2));
    }
}
