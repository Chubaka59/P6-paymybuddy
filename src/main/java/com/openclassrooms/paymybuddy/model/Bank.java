package com.openclassrooms.paymybuddy.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Table(name = "bank")
@Data
public class Bank {
    @Id
    @Column(name = "user_id")
    private int userId;

    @Column(name = "bank_account")
    private String bankAccount;

    @Column(name = "balance")
    private BigDecimal balance;
}
