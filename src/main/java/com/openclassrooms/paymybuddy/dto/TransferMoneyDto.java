package com.openclassrooms.paymybuddy.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferMoneyDto {

    @NotEmpty
    String contactEmail;

    @NotNull
    @Digits(integer = 3, fraction = 2)
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount can not be equal less then 0")
    BigDecimal amount;

    String description;
}
