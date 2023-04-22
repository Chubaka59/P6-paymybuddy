package com.openclassrooms.paymybuddy.dto;

import jakarta.validation.constraints.DecimalMin;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ReloadDto {
    BigDecimal amount;
}
