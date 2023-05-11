package com.openclassrooms.paymybuddy.model;

import com.openclassrooms.paymybuddy.dto.UserAccountCreationDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    private Integer id;

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

    private BigDecimal balance = BigDecimal.valueOf(0);

    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST
    )
    private List<UserAccount> contactList = new ArrayList<>();


    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }

    public UserAccount(UserAccountCreationDto userAccountCreationDto, PasswordEncoder passwordEncoder){
        this.setFirstName(userAccountCreationDto.getFirstName());
        this.setLastName(userAccountCreationDto.getLastName());
        this.setEmail(userAccountCreationDto.getEmail());
        this.setBank(userAccountCreationDto.getBank());
        //encrypt password
        this.setPassword(passwordEncoder.encode(userAccountCreationDto.getPassword()));
    }

    public UserAccount creditBalance(BigDecimal amount){
        if (balance.add(amount).compareTo(BigDecimal.ZERO) < 0){
            throw new RuntimeException("The balance is insufficient");
        }
        setBalance(balance.add(amount));
        return this;
    }

    public UserAccount debitBalance(BigDecimal amount){
        if(amount.compareTo(BigDecimal.ZERO) <= 0 ){
            throw new RuntimeException("Amount can not be equal less then 0");
        }

        if(balance.compareTo(amount) < 0 ) {
            throw new RuntimeException("The balance is insufficient");
        }
        balance = balance.subtract(amount);

        return this;
    }

    public UserAccount addContact(UserAccount userAccount){
        if (contactList.contains(userAccount)) {
            throw new RuntimeException(userAccount.getEmail() + " is already in your contact");
        }
        contactList.add(userAccount);
        return this;
    }
}
