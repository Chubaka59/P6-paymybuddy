package com.openclassrooms.paymybuddy.dto;

import com.openclassrooms.paymybuddy.model.Transaction;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class TransactionDto {
    private LocalDate date;

    private BigDecimal amount;

    @Size(max = 300)
    private String description;

    private String creditor;

    private String debtor;

    public TransactionDto(Transaction transaction){
        date = transaction.getDate();
        amount = transaction.getAmount();
        creditor = transaction.getCreditor().getFullName();
        debtor = transaction.getDebtor().getFullName();
        description = transaction.getDescription();
    }
}
