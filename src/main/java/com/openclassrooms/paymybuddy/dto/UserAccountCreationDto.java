package com.openclassrooms.paymybuddy.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
public class UserAccountCreationDto {
    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private String bank;
}
