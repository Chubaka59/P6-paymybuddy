package com.openclassrooms.paymybuddy.model;

import com.openclassrooms.paymybuddy.dto.TransferMoneyDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
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
    private Integer id;

    private LocalDate date;

    private BigDecimal originalAmount;

    private BigDecimal amount;

    @Size(max = 300)
    private String description;

    @ManyToOne
    private UserAccount creditor;

    @ManyToOne
    private UserAccount debtor;

    public Transaction(TransferMoneyDto transferMoneyDto, UserAccount creditor, UserAccount debtor){
        if(transferMoneyDto.getAmountWithFee().compareTo(BigDecimal.ZERO) <= 0 ){
            throw new RuntimeException("Amount can not be equal less then 0");
        }
        this.date = LocalDate.now();
        this.amount = transferMoneyDto.getAmountWithFee();
        this.originalAmount = transferMoneyDto.getAmount();
        this.description = transferMoneyDto.getDescription();
        this.creditor = creditor;
        this.debtor= debtor;
    }
}
