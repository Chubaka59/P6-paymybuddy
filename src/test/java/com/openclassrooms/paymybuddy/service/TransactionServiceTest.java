package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.dto.TransactionDto;
import com.openclassrooms.paymybuddy.model.Transaction;
import com.openclassrooms.paymybuddy.model.UserAccount;
import com.openclassrooms.paymybuddy.repository.TransactionRepository;
import com.openclassrooms.paymybuddy.repository.UserAccountRepository;
import com.openclassrooms.paymybuddy.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TransactionServiceTest {
    @InjectMocks
    private TransactionServiceImpl transactionService;
    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private UserAccountRepository userAccountRepository;
    @Mock
    Pageable pageable;

    @Test
    public void findTransactionByUserTest(){
        //GIVEN there is transaction to find
        UserAccount existingUser = new UserAccount(1, "test", "test", null, null, null, null, null);
        when(userAccountRepository.findByEmail(anyString())).thenReturn(Optional.of(existingUser));
        Transaction transaction = new Transaction(1, LocalDate.now(), amountWithFee(BigDecimal.ONE), BigDecimal.ONE, null, existingUser, new UserAccount());
        when(transactionRepository.findAllByDebtorOrCreditorOrderByDateDesc(existingUser, existingUser)).thenReturn(List.of(transaction));

        List<TransactionDto> expectedTransactionDtoList = List.of(new TransactionDto(transaction));

        //WHEN we would find the transactions
        List<TransactionDto> actualTransactionDtoList = transactionService.findTransactionByUser("test");

        //THEN a list is returned
        assertEquals(expectedTransactionDtoList, actualTransactionDtoList);
    }

    @Test
    public void findTransactionByUserWhenUserIsNotFound(){
        //GIVEN the use is not found
        when(userAccountRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        //WHEN we call the method THEN an exception is thrown
        Assertions.assertThrows(UsernameNotFoundException.class, () -> transactionService.findTransactionByUser("test"));
    }

    @Test
    public void findPaginatedWhenOnlyOnePageTest(){
        //GIVEN there is not enough transactions to get more than one page
        int currentPage = 1;
        int pageSize = 5;
        UserAccount userAccount = new UserAccount(1, "test", "test", "test", "test", "test", BigDecimal.ONE, null);
        Transaction transaction = new Transaction(1, LocalDate.now(), amountWithFee(BigDecimal.ONE), BigDecimal.ONE, "test", new UserAccount(), new UserAccount());
        when(userAccountRepository.findByEmail("test")).thenReturn(Optional.of(userAccount));
        when(transactionRepository.findAllByDebtorOrCreditorOrderByDateDesc(userAccount, userAccount)).thenReturn(List.of(transaction));
        TransactionDto transactionDto = new TransactionDto(transaction);
        Page<TransactionDto> expectedTransactionDtoPage = new PageImpl<>(Collections.emptyList(), PageRequest.of(currentPage, pageSize), List.of(transactionDto).size());

        //WHEN we get the page
        Page<TransactionDto> actualTransactionDtoPage = transactionService.findPaginated(PageRequest.of(currentPage, pageSize), "test");

        //THEN we get the correct return
        assertEquals(expectedTransactionDtoPage, actualTransactionDtoPage);
    }

    @Test
    public void findPaginatedWhenMoreThanOnePageTest(){
        //GIVEN there is more than one page of transactions
        int currentPage = 2;
        int pageSize = 5;
        UserAccount userAccount = new UserAccount(1, "test", "test", "test", "test", "test", BigDecimal.ONE, null);
        List<Transaction> transactionList = new ArrayList<>();
        List<TransactionDto> transactionDtoList = new ArrayList<>();
        for(int i=0; i<10; i++ ){
            Transaction transaction = new Transaction(i, LocalDate.now(), amountWithFee(BigDecimal.ONE), BigDecimal.ONE, "test", new UserAccount(), new UserAccount());
            transactionList.add(transaction);
            transactionDtoList.add(new TransactionDto(transaction));
        }
        when(userAccountRepository.findByEmail("test")).thenReturn(Optional.of(userAccount));
        when(transactionRepository.findAllByDebtorOrCreditorOrderByDateDesc(userAccount, userAccount)).thenReturn(transactionList);
        Page<TransactionDto> expectedTransactionDtoPage = new PageImpl<>(transactionDtoList.subList(10, 10), PageRequest.of(currentPage, pageSize), transactionDtoList.size());

        //WHEN we get the page
        Page<TransactionDto> actualTransactionDtoPage = transactionService.findPaginated(PageRequest.of(currentPage, pageSize), "test");

        //THEN we get the correct return
        assertEquals(expectedTransactionDtoPage, actualTransactionDtoPage);
    }

    private BigDecimal amountWithFee(BigDecimal amount){
        return amount.multiply(BigDecimal.valueOf(0.005));
    }
}
