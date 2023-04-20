package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.dto.TransactionDto;

import java.util.List;

public interface TransactionService {
    List<TransactionDto> findTransactionByUser(String username);
    List<Transaction> findAllByCreditor(String Creditor);
    List<Transaction> findAllByDebtor(String Debtor);
    String getNameFromUsername(String username);
}
