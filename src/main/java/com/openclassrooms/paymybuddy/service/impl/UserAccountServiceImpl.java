package com.openclassrooms.paymybuddy.service.impl;

import com.openclassrooms.paymybuddy.dto.*;
import com.openclassrooms.paymybuddy.model.Transaction;
import com.openclassrooms.paymybuddy.model.UserAccount;
import com.openclassrooms.paymybuddy.repository.TransactionRepository;
import com.openclassrooms.paymybuddy.repository.UserAccountRepository;
import com.openclassrooms.paymybuddy.service.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class UserAccountServiceImpl implements UserAccountService {
    @Autowired
    private UserAccountRepository userAccountRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    @Override
    public Iterable<UserAccount> getUsers() {
        return userAccountRepository.findAll();
    }

    @Override
    public Optional<UserAccount> getUserById(Integer id) {
        return userAccountRepository.findById(id);
    }

    @Override
    public void saveUserAccount(UserAccountCreationDto userAccountCreationDto) {
        UserAccount userAccount = new UserAccount(userAccountCreationDto);
        userAccountRepository.save(userAccount);
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
    public UserAccount addContact(UserAccount userAccount, String email) {
        Optional<UserAccount> contactToAdd = userAccountRepository.findByEmail(userAccount.getEmail());
        if (contactToAdd.isEmpty()){
            throw new UsernameNotFoundException("User not found with email : " + userAccount.getEmail());
        }

        Optional<UserAccount> userAccountOptToUpdate = userAccountRepository.findByEmail(email);
        if (userAccountOptToUpdate.isEmpty()){
            throw new UsernameNotFoundException("User not found with email : " + email);
        }

        UserAccount userAccountToUpdate = userAccountOptToUpdate.get();
        userAccountToUpdate.getContactList().add(contactToAdd.get());
        return userAccountRepository.save(userAccountToUpdate);
    }

    @Override
    public BigDecimal getBalance(String email) {
        Optional<UserAccount> userAccount = userAccountRepository.findByEmail(email);
        if (userAccount.isEmpty()) {
            throw new UsernameNotFoundException("User not found with email : " + email);
        }
        return userAccount.get().getBalance();
    }

    @Override
    public UserAccount reloadBalance(ReloadDto reloadDto, String email) {
        Optional<UserAccount> userAccount = userAccountRepository.findByEmail(email);
        if (userAccount.isEmpty()) {
            throw new UsernameNotFoundException("User not found with email : " + email);
        }
        userAccount.get().reload(reloadDto);
        return userAccountRepository.save(userAccount.get());
    }

    @Override
    public void transferMoney(TransferMoneyDto transferMoneyDto, String email) {
        Optional<UserAccount> creditor = findUserByEmail(email);
        Optional<UserAccount> debtor = findUserByEmail(transferMoneyDto.getContactEmail());

        Transaction transaction = new Transaction(transferMoneyDto, creditor.get(), debtor.get());
        creditor.get().updateBalance(transaction.getAmount().negate());
        debtor.get().updateBalance(transaction.getAmount());
        userAccountRepository.save(creditor.get());
        userAccountRepository.save(debtor.get());
        transactionRepository.save(transaction);
    }

    @Override
    public boolean hasNotEnoughBalance(BigDecimal amount, String email) {
        Optional<UserAccount> userAccount = userAccountRepository.findByEmail(email);
        if (userAccount.isEmpty()) {
            throw new UsernameNotFoundException("User not found with email : " + email);
        }
        int result = amount.compareTo(userAccount.get().getBalance());
        return result > 0 ;
    }
}
