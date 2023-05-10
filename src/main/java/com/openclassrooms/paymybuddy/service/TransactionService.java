package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.dto.TransactionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TransactionService {
    /**
     * find all transactions from a specific user
     * @param email the email of the user
     * @return a list of transactions
     */
    List<TransactionDto> findTransactionByUser(String email);

    /**
     * get the pagination of the transaction list from a specific user
     * @param pageable
     * @param email the email of the user
     * @return the pagination of the transaction list
     */
    Page<TransactionDto> findPaginated(Pageable pageable, String email);
}
