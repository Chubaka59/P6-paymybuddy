package com.openclassrooms.paymybuddy.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BankDto {
    @NotNull
    @Digits(integer = 3, fraction = 2)
    BigDecimal amount;
}
