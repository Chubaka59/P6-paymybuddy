package com.openclassrooms.paymybuddy.service.impl;

import com.openclassrooms.paymybuddy.dto.TransactionDto;
import com.openclassrooms.paymybuddy.model.Transaction;
import com.openclassrooms.paymybuddy.model.UserAccount;
import com.openclassrooms.paymybuddy.repository.TransactionRepository;
import com.openclassrooms.paymybuddy.repository.UserAccountRepository;
import com.openclassrooms.paymybuddy.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserAccountRepository userAccountRepository;

    @Override
    public List<TransactionDto> findTransactionByUser(String email) {
       UserAccount connectedUser = userAccountRepository.findByEmail(email)
                .orElseThrow(()->  new UsernameNotFoundException("User not found with email : " + email));

        return transactionRepository.findAllByDebtorOrCreditorOrderByDateAsc(connectedUser, connectedUser)
                .stream()
                .map(TransactionDto::new)
                .toList();
    }

    @Override
    public List<Transaction> findAllByCreditor(String Creditor) {
        return transactionRepository.findAllByCreditor(Creditor);
    }

    @Override
    public List<Transaction> findAllByDebtor(String Debtor) {
        return transactionRepository.findAllByDebtor(Debtor);
    }

    @Override
    public String getNameFromUsername(String username) {
        Optional<UserAccount> userAccountOptional = userAccountRepository.findByEmail(username);
        if (userAccountOptional.isEmpty()) {
            throw new UsernameNotFoundException("User not found with email : " + username);
        } else {
            return userAccountOptional.get().getFirstName() + " " + userAccountOptional.get().getLastName();
        }
    }
}
