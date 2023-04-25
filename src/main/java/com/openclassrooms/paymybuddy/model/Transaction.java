package com.openclassrooms.paymybuddy.model;

import com.openclassrooms.paymybuddy.dto.TransferMoneyDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private LocalDate date;

    private BigDecimal amount;

    private String description;

    @ManyToOne
    private UserAccount creditor;

    @ManyToOne
    private UserAccount debtor;

    public Transaction(TransferMoneyDto transferMoneyDto, UserAccount creditor, UserAccount debtor){
        date = LocalDate.now();
        amount = transferMoneyDto.getAmount();
        description = transferMoneyDto.getDescription();
        this.creditor = creditor;
        this.debtor= debtor;
    }
}
