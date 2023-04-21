package com.openclassrooms.paymybuddy.service.impl;

import com.openclassrooms.paymybuddy.dto.TransactionDto;
import com.openclassrooms.paymybuddy.model.UserAccount;
import com.openclassrooms.paymybuddy.repository.TransactionRepository;
import com.openclassrooms.paymybuddy.repository.UserAccountRepository;
import com.openclassrooms.paymybuddy.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
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

        return transactionRepository.findAllByDebtorOrCreditorOrderByDateDesc(connectedUser, connectedUser)
                .stream()
                .map(TransactionDto::new)
                .toList();
    }

    @Override
    public Page<TransactionDto> findPaginated(Pageable pageable, String email) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<TransactionDto> list;

        List<TransactionDto> transactionDtoList = findTransactionByUser(email);
        if (transactionDtoList.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, transactionDtoList.size());
            list = transactionDtoList.subList(startItem, toIndex);
        }

        return new PageImpl<>(list, PageRequest.of(currentPage, pageSize), transactionDtoList.size());
    }


}
