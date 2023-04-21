package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.dto.TransactionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TransactionService {
    List<TransactionDto> findTransactionByUser(String email);
    Page<TransactionDto> findPaginated(Pageable pageable, String email);
}
