package com.openclassrooms.paymybuddy.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Data
public class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "lastname")
    private String lastName;

    @Column(name = "mail")
    private String mail;

    @Column(name = "password")
    private String password;

    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST
    )
    private List<UserAccount> contactList = new ArrayList<>();

    @OneToOne(cascade = CascadeType.PERSIST)
    private Bank bank;
}
