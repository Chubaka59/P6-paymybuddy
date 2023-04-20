package com.openclassrooms.paymybuddy.service.impl;

import com.openclassrooms.paymybuddy.dto.TransactionDto;
import com.openclassrooms.paymybuddy.model.UserAccount;
import com.openclassrooms.paymybuddy.repository.TransactionRepository;
import com.openclassrooms.paymybuddy.repository.UserAccountRepository;
import com.openclassrooms.paymybuddy.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
