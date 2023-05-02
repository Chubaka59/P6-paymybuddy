package com.openclassrooms.paymybuddy.service;

import com.openclassrooms.paymybuddy.dto.BankDto;
import com.openclassrooms.paymybuddy.dto.ContactDto;
import com.openclassrooms.paymybuddy.dto.TransferMoneyDto;
import com.openclassrooms.paymybuddy.dto.UserAccountCreationDto;
import com.openclassrooms.paymybuddy.exception.UsernameAlreadyExistException;
import com.openclassrooms.paymybuddy.model.Transaction;
import com.openclassrooms.paymybuddy.model.UserAccount;
import com.openclassrooms.paymybuddy.repository.TransactionRepository;
import com.openclassrooms.paymybuddy.repository.UserAccountRepository;
import com.openclassrooms.paymybuddy.service.impl.UserAccountServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserAccountServiceTest {
    @InjectMocks
    private UserAccountServiceImpl userAccountService;
    @Mock
    private UserAccountRepository userAccountRepository;
    @Mock
    private TransactionRepository transactionRepository;

    @Test
    public void saveUserAccountTest() {
        //GIVEN we would save a user
        UserAccountCreationDto userAccountCreationDto = new UserAccountCreationDto();
        userAccountCreationDto.setFirstName("test");
        userAccountCreationDto.setLastName("test");
        userAccountCreationDto.setEmail("test@test.test");
        userAccountCreationDto.setBank("test");
        userAccountCreationDto.setPassword("test");

        UserAccount expectedUser = new UserAccount(userAccountCreationDto, new BCryptPasswordEncoder());

        when(userAccountRepository.save(any(UserAccount.class))).thenReturn(expectedUser);

        //WHEN the method is called
        userAccountService.saveUserAccount(userAccountCreationDto);

        //THEN the method to save in the repository is called
        verify(userAccountRepository, times(1)).save(any(UserAccount.class));
    }

    @Test
    public void saveUserAccountWhenUserAlreadyExistTest(){
        //GIVEN the user we would save already exist
        when(userAccountRepository.findByEmail(anyString())).thenReturn(Optional.of(new UserAccount()));
        UserAccountCreationDto userAccountCreationDto = new UserAccountCreationDto("test", "test", "test", "test", "test");

        //WHEN we would save the user THEN an exception is thrown
        assertThrows(UsernameAlreadyExistException.class, () -> userAccountService.saveUserAccount(userAccountCreationDto));
    }

    @Test
    public void findUserByEmailTest() {
        //GIVEN there is a user to find
        when(userAccountRepository.findByEmail(anyString())).thenReturn(any(Optional.class));

        //WHEN the method is called
        userAccountService.findUserByEmail("test");

        //THEN the method userAccountRepository.findByEmail(email) is called once
        verify(userAccountRepository, times(1)).findByEmail(anyString());
    }

    @Test
    public void findContactListTest() {
        //GIVEN there is a contact list to return
        UserAccount userAccount = new UserAccount();
        UserAccount contact = new UserAccount();
        userAccount.getContactList().add(contact);
        List<ContactDto> expectedContactList = List.of(new ContactDto(contact));
        when(userAccountRepository.findByEmail(anyString())).thenReturn(Optional.of(userAccount));

        //WHEN we get the contact list
        List<ContactDto> contactDtoList = userAccountService.findContactList("test");

        //THEN the contactList is returned
        Assertions.assertEquals(expectedContactList, contactDtoList);
    }

    @Test
    public void findContactListWhenContactIsNotFoundTest() {
        //GIVEN the contact is not found
        when(userAccountRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        //WHEN the method is called THEN an exception is raised
        assertThrows(UsernameNotFoundException.class, () -> userAccountService.findContactList("test"));
    }

    @Test
    public void addContact() {
        //GIVEN there is an account and a contact to add
        UserAccount contact = new UserAccount(1, "contact", "contact", "contact", "contact", "contact", BigDecimal.ONE, null);
        UserAccount userAccount = new UserAccount(2, "test", "test", "test", "test", "test", BigDecimal.ONE, new ArrayList<>());
        ContactDto contactDto = new ContactDto("contact", "contact");
        when(userAccountRepository.findByEmail("test")).thenReturn(Optional.of(userAccount));
        when(userAccountRepository.findByEmail("contact")).thenReturn(Optional.of(contact));
        when(userAccountRepository.save(any(UserAccount.class))).thenReturn(userAccount);

        //WHEN we add the contact the the contact list of the user
        userAccountService.addContact(contactDto, "test");

        //THEN the user is saved with the new contact
        verify(userAccountRepository, times(1)).save(userAccount);
    }

    @Test
    public void addContactWhenContactIsNotFoundTest(){
        //GIVEN the contact is not found
        ContactDto contactDto = new ContactDto("contact", "contact");
        when(userAccountRepository.findByEmail("contact")).thenReturn(Optional.empty());

        //WHEN the method is called then an exception is thrown
        assertThrows(UsernameNotFoundException.class, () -> userAccountService.addContact(contactDto, "test"));
    }

    @Test
    public void addContactWhenUserIsNotFoundTest(){
        //GIVEN the user is not found
        ContactDto contactDto = new ContactDto("contact", "contact");
        UserAccount userAccount = new UserAccount(1, "contact", "contact", "contact", "contact", "contact",BigDecimal.ONE, null);
        when(userAccountRepository.findByEmail(anyString())).thenReturn(Optional.of(userAccount));
        when(userAccountRepository.findByEmail(userAccount.getEmail())).thenReturn(Optional.empty());

        //WHEN the method is called then an exception is thrown
        assertThrows(UsernameNotFoundException.class, () -> userAccountService.addContact(contactDto, "test"));
    }

    @Test
    public void getBalanceTest(){
        //GIVEN we would get the balance of a user
        UserAccount userAccount = new UserAccount(1, "test", "test", "test", "test", "test", BigDecimal.ONE, null);
        when(userAccountRepository.findByEmail("test")).thenReturn(Optional.of(userAccount));

        //WHEN we call the method to get the balance
        BigDecimal actualBalance = userAccountService.getBalance("test");

        //THEN we get the correct balance
        assertEquals(BigDecimal.ONE, actualBalance);
    }

    @Test
    public void getBalanceWhenUserIsNotFoundTest(){
        //GIVEN the user we would get the balance doesn't exist
        when(userAccountRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        //WHEN we call the method THEN an error is thrown
        assertThrows(UsernameNotFoundException.class, () -> userAccountService.getBalance("test"));
    }

    @Test
    public void bankTransferTest(){
        //GIVEN we would transfer money from bank
        UserAccount userAccount = new UserAccount(1, "test", "test", "test", "test", "test", BigDecimal.ZERO, null);
        UserAccount expectedUserAccount = new UserAccount(1, "test", "test", "test", "test", "test", BigDecimal.valueOf(100), null);
        when(userAccountRepository.findByEmail(anyString())).thenReturn(Optional.of(userAccount));
        BankDto bankDto = new BankDto(BigDecimal.valueOf(100));
        when(userAccountRepository.save(expectedUserAccount)).thenReturn(expectedUserAccount);

        //WHEN we would credit the balance
        userAccountService.bankTransfer(bankDto, "test");

        //THEN the updated user account is saved
        verify(userAccountRepository, times(1)).save(expectedUserAccount);
    }

    @Test
    public void bankTransferWhenUserIsNotFoundTest(){
        //GIVEN the user won't be found
        when(userAccountRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        //WHEN the method is called THEN an error is thrown
        assertThrows(UsernameNotFoundException.class, () -> userAccountService.bankTransfer(new BankDto(), "test"));
    }

    @Test
    public void transferMoneyTest(){
        //GIVEN the user is able to transfer money
        TransferMoneyDto transferMoneyDto = new TransferMoneyDto("contact", BigDecimal.valueOf(75), null);
        UserAccount debtor = new UserAccount(1, "contact", "contact", "contact", "contact", "contact", BigDecimal.ZERO, null);
        UserAccount creditor = new UserAccount(2, "test", "test", "test", "test", "test", BigDecimal.valueOf(100), null);
        Transaction expectedTransaction = new Transaction(transferMoneyDto, creditor, debtor);


        when(userAccountRepository.findByEmail("test")).thenReturn(Optional.of(creditor));
        when(userAccountRepository.findByEmail("contact")).thenReturn(Optional.of(debtor));
        when(userAccountRepository.save(any(UserAccount.class))).thenAnswer(a -> a.getArguments()[0]);
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(a -> a.getArguments()[0]);


        //WHEN we make the transfer
        Transaction response = userAccountService.transferMoney(transferMoneyDto, "test");

        //THEN the user, the contact and the transaction are saved
        assertThat(response)
                .satisfies(r -> {
                    assertThat(response.getCreditor().getId()).isEqualTo(creditor.getId());
                    assertThat(response.getCreditor().getBalance()).isEqualTo(BigDecimal.valueOf(100).subtract(transferMoneyDto.getAmountWithFee()));

                    assertThat(response.getDebtor().getId()).isEqualTo(debtor.getId());
                    assertThat(response.getDebtor().getBalance()).isEqualTo(transferMoneyDto.getAmount());
                });
        verify(userAccountRepository, times(2)).save(any(UserAccount.class));
        verify(transactionRepository, times(1)).save(expectedTransaction);
    }

    @Test
    public void transferMoneyWhenDebtorIsNotFoundTest(){
        //GIVEN the debtor is not found
        TransferMoneyDto transferMoneyDto = new TransferMoneyDto("contact", BigDecimal.valueOf(50), null);
        when(userAccountRepository.findByEmail("contact")).thenReturn(Optional.empty());

        //WHEN the method is called THEN an exception is thrown
        assertThrows(UsernameNotFoundException.class, () -> userAccountService.transferMoney(transferMoneyDto, "test"));
    }

    @Test
    public void transferMoneyWhenCreditorIsNotFoundTest(){
        //GIVEN the creditor is not found
        TransferMoneyDto transferMoneyDto = new TransferMoneyDto("contact", BigDecimal.valueOf(50), null);
        UserAccount contact = new UserAccount(1, "contact", "contact", "contact", "contact", "contact", BigDecimal.ZERO, null);
        when(userAccountRepository.findByEmail("contact")).thenReturn(Optional.of(contact));
        when(userAccountRepository.findByEmail("test")).thenReturn(Optional.empty());

        //WHEN the method is called THEN an exception is thrown
        assertThrows(UsernameNotFoundException.class, () -> userAccountService.transferMoney(transferMoneyDto, "test"));
    }
}
