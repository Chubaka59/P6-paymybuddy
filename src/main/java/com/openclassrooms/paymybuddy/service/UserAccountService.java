package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.dto.ContactDto;
import com.openclassrooms.paymybuddy.dto.ReloadDto;
import com.openclassrooms.paymybuddy.dto.TransferMoneyDto;
import com.openclassrooms.paymybuddy.dto.UserAccountCreationDto;
import com.openclassrooms.paymybuddy.model.UserAccount;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface UserAccountService {

    void saveUserAccount(UserAccountCreationDto userAccountDto);

    Optional<UserAccount> findUserByEmail(String email);

    List<ContactDto> findContactList(String email);

    UserAccount addContact(UserAccount userAccount, String email);

    BigDecimal getBalance(String email);

    UserAccount reloadBalance(ReloadDto reloadDto, String name);

    void transferMoney(TransferMoneyDto transferMoneyDto, String email);

    boolean hasNotEnoughBalance(BigDecimal amount, String email);
}
