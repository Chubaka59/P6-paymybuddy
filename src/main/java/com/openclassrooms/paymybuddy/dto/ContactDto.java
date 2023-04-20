package com.openclassrooms.paymybuddy.dto;

import com.openclassrooms.paymybuddy.model.UserAccount;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactDto {
    @Email
    String email;

    String name;

    public ContactDto(UserAccount userAccount){
        email = userAccount.getEmail();
        name = userAccount.getFullName();
    }
}
