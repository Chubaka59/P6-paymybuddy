package com.openclassrooms.paymybuddy.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
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
}
