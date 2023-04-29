package com.openclassrooms.paymybuddy.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankDto {
    @NotNull
    @Digits(integer = 3, fraction = 2)
    BigDecimal amount;
}
