package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.dto.ContactDto;
import com.openclassrooms.paymybuddy.dto.BankDto;
import com.openclassrooms.paymybuddy.dto.TransferMoneyDto;
import com.openclassrooms.paymybuddy.dto.UserAccountCreationDto;
import com.openclassrooms.paymybuddy.model.Transaction;
import com.openclassrooms.paymybuddy.model.UserAccount;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface UserAccountService {

    UserAccount saveUserAccount(UserAccountCreationDto userAccountDto);

    Optional<UserAccount> findUserByEmail(String email);

    List<ContactDto> findContactList(String email);

    UserAccount addContact(ContactDto contactDto, String email);

    BigDecimal getBalance(String email);

    UserAccount bankTransfer(BankDto bankDto, String name);

    Transaction transferMoney(TransferMoneyDto transferMoneyDto, String email);
}
