package com.openclassrooms.paymybuddy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class TransactionDto {
    private LocalDate date;

    private BigDecimal amount;

    private String description;

    private String creditor;

    private String debtor;
}
