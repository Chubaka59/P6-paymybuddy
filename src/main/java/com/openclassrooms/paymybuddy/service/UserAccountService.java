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
    /**
     * save a new UserAccount in DB
     * @param userAccountDto the information of the new user
     * @return return the UserAccount
     */
    UserAccount saveUserAccount(UserAccountCreationDto userAccountDto);

    /**
     * find a UserAccount from its email
     * @param email the email used to find the user
     * @return the UserAccount
     */
    Optional<UserAccount> findUserByEmail(String email);

    /**
     * find the ContactList of a user
     * @param email the email used to find the user's contact list
     * @return a list of contact
     */
    List<ContactDto> findContactList(String email);

    /**
     * add a contact to the contact list of a user
     * @param contactDto the contact information to add
     * @param email the email of the user to add contact to his list
     * @return the UserAccount of the contact
     */
    UserAccount addContact(ContactDto contactDto, String email);

    /**
     * get the balance of a user
     * @param email the email of the user to get the balance
     * @return a bigdecimal of the balance
     */
    BigDecimal getBalance(String email);

    /**
     * credit or debit the balance of a user from or to his bank account
     * @param bankDto the information of the transaction
     * @param email the email of the user to credit or debit its balance
     * @return the UserAccount of the user
     */
    UserAccount bankTransfer(BankDto bankDto, String email);

    /**
     * credit the balance of the debtor, debit the balance of the creditor and create a new transaction in DB
     * @param transferMoneyDto the information of the transaction
     * @param email the email of the creditor
     * @return the transaction
     */
    Transaction transferMoney(TransferMoneyDto transferMoneyDto, String email);
}
