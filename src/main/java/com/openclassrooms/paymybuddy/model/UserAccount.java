package com.openclassrooms.paymybuddy.model;

import com.openclassrooms.paymybuddy.dto.ReloadDto;
import com.openclassrooms.paymybuddy.dto.UserAccountCreationDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String bank;

    @Column(nullable = false)
    private BigDecimal balance;

    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST
    )
    private List<UserAccount> contactList = new ArrayList<>();

    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    public UserAccount(UserAccountCreationDto userAccountCreationDto){
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        this.setFirstName(userAccountCreationDto.getFirstName());
        this.setLastName(userAccountCreationDto.getLastName());
        this.setEmail(userAccountCreationDto.getEmail());
        this.setBank(userAccountCreationDto.getBank());
        //encrypt password
        this.setPassword(passwordEncoder.encode(userAccountCreationDto.getPassword()));
    }

    public void reload(ReloadDto reloadDto){
       balance = balance.add(reloadDto.getAmount()).setScale(2);
    }

    public void updateBalance(BigDecimal amount){
        setBalance(balance.add(amount));
    }
}
