package com.openclassrooms.paymybuddy.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferMoneyDto {

    @NotEmpty
    String contactEmail;

    @NotNull
    @DecimalMin(value = "0.01", message = "Amount can not be equal less then 0")
    BigDecimal amount = BigDecimal.ZERO;

    String description;

    public BigDecimal getAmountWithFee(){
        final BigDecimal fee = amount.multiply(BigDecimal.valueOf(0.005));
        return amount.add(fee);
    }
}
