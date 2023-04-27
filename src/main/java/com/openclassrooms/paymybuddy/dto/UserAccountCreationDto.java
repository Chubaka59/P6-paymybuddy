package com.openclassrooms.paymybuddy.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserAccountCreationDto {
    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String bank;
}
