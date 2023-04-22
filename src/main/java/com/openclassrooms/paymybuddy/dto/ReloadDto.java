package com.openclassrooms.paymybuddy.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ReloadDto {
    @Digits(integer = 3, fraction = 2)
    BigDecimal amount;
}
