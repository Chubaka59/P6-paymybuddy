package com.openclassrooms.paymybuddy.dto;

import com.openclassrooms.paymybuddy.model.UserAccount;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserBalanceDto {
    private BigDecimal balance;

    public UserBalanceDto(UserAccount userAccount){
        balance = userAccount.getBalance();
    }
}
