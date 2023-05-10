package com.openclassrooms.paymybuddy.service.impl;

import com.openclassrooms.paymybuddy.dto.*;
import com.openclassrooms.paymybuddy.exception.UsernameAlreadyExistException;
import com.openclassrooms.paymybuddy.model.Transaction;
import com.openclassrooms.paymybuddy.model.UserAccount;
import com.openclassrooms.paymybuddy.repository.TransactionRepository;
import com.openclassrooms.paymybuddy.repository.UserAccountRepository;
import com.openclassrooms.paymybuddy.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class UserAccountServiceImpl implements UserAccountService {
    @Autowired
    private UserAccountRepository userAccountRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public UserAccount saveUserAccount(UserAccountCreationDto userAccountCreationDto) {
        Optional<UserAccount> existingUser = findUserByEmail(userAccountCreationDto.getEmail());
        if (existingUser.isPresent()){
            throw new UsernameAlreadyExistException(userAccountCreationDto.getEmail());
        }
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        UserAccount userAccount = new UserAccount(userAccountCreationDto, passwordEncoder);
        return userAccountRepository.save(userAccount);
    }

    @Override
    public Optional<UserAccount> findUserByEmail(String email) {
        return userAccountRepository.findByEmail(email);
    }

    @Override
    public List<ContactDto> findContactList(String email) {
        Optional<UserAccount> userAccountOptToGetContact = userAccountRepository.findByEmail(email);
        if (userAccountOptToGetContact.isEmpty()){
            throw new UsernameNotFoundException("User not found with email : " + email);
        }
        return userAccountOptToGetContact.get().getContactList().stream()
                .map(ContactDto::new)
                .toList();
    }

    @Override
    public UserAccount addContact(ContactDto contactDto, String email) {
        UserAccount contactToAdd = userAccountRepository.findByEmail(contactDto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email : " + contactDto.getEmail()));

        UserAccount userAccountToUpdate = userAccountRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email : " + email))
                .addContact(contactToAdd);
        return userAccountRepository.save(userAccountToUpdate);
    }

    @Override
    public BigDecimal getBalance(String email) {
        return userAccountRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Account not found with email : " + email))
                .getBalance();
    }

    @Override
    public UserAccount bankTransfer(BankDto bankDto, String email) {
        final UserAccount userAccount = findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email))
                .creditBalance(bankDto.getAmount());
        return userAccountRepository.save(userAccount);
    }

    @Override
    public Transaction transferMoney(TransferMoneyDto transferMoneyDto, String email) {
        // Find debtor account and debit transaction amount without fees ( getAmount() )
        final UserAccount debtor = findUserByEmail(transferMoneyDto.getContactEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Contact not found with email : "+ transferMoneyDto.getContactEmail()))
                .creditBalance(transferMoneyDto.getAmount());
        userAccountRepository.save(debtor);

        // Find creditor account and credit transaction amount with fees ( getOriginalAmount() )
        final UserAccount creditor = findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Account not found with email : " + email))
                .debitBalance(transferMoneyDto.getAmountWithFee());

        userAccountRepository.save(creditor);

        Transaction transaction = new Transaction(transferMoneyDto, creditor, debtor);

        return transactionRepository.save(transaction);
    }
}
