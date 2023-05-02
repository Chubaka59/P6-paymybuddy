package com.openclassrooms.paymybuddy.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserAccountTest {
    @Test
    public void getFullNameTest(){
        //GIVEN the following user account exist
        UserAccount existingUserAccount = new UserAccount(1, "testfirstname", "testlastname", "test", "test", "test", BigDecimal.ZERO, null);
        String expectedFullName = existingUserAccount.getFirstName() + " " + existingUserAccount.getLastName();

        //WHEN we get the full name
        String actualFullName = existingUserAccount.getFullName();

        //THEN the full name is correct
        assertEquals(expectedFullName, actualFullName);
    }

    @Test
    public void creditBalanceTest(){
        //GIVEN we would credit the balance
        UserAccount existingUserAccount = new UserAccount(1, "test", "test", "test", "test", "test", BigDecimal.ZERO, null);

        //WHEN we credit the balance
        existingUserAccount.creditBalance(BigDecimal.valueOf(100));

        //THEN the balance get the amount
        assertEquals(BigDecimal.valueOf(100), existingUserAccount.getBalance());
    }

    @Test
    public void creditBalanceWhenBalanceIsInsufficientTest(){
        //GIVEN we would credit of a negative amount
        UserAccount existingUserAccount = new UserAccount(1, "test", "test", "test", "test", "test", BigDecimal.ONE, null);

        //WHEN we transfer the amount THEN an exception is thrown
        assertThrows(RuntimeException.class, () -> existingUserAccount.creditBalance(BigDecimal.valueOf(-2)));
    }

    @Test
    public void debitBalanceTest(){
        //GIVEN we would debit the balance
        UserAccount existingUserAccount = new UserAccount(1, "test", "test", "test", "test", "test", BigDecimal.ONE, null);

        //WHEN we debit the balance
        existingUserAccount.debitBalance(BigDecimal.ONE);

        //THEN the balance is updated
        assertEquals(BigDecimal.ZERO, existingUserAccount.getBalance());
    }

    @Test
    public void debitBalanceWhenAmountIsLessThanZeroTest(){
        //GIVEN We would debit a negative amount
        UserAccount existingUserAccount = new UserAccount(1, "test", "test", "test", "test", "test", BigDecimal.ONE, null);

        //WHEN we debit the amount THEN an error is thrown
        assertThrows(RuntimeException.class, () -> existingUserAccount.debitBalance(BigDecimal.valueOf(-1)));
    }

    @Test
    public void debitBalanceWhenBalanceIsInsufficientTest(){
        //GIVEN the balance is insufficient
        UserAccount existingUserAccount = new UserAccount(1, "test", "test", "test", "test", "test", BigDecimal.ZERO, null);

        //WHEN we debit the balance THEN an error is thrown
        assertThrows(RuntimeException.class, () -> existingUserAccount.debitBalance(BigDecimal.ONE));
    }

    @Test
    public void addContactTest(){
        //GIVEN We would add a contact
        UserAccount existingUserAccount = new UserAccount(1, "test", "test", "test", "test", "test", BigDecimal.ZERO, new ArrayList<>());
        UserAccount contactToAdd = new UserAccount(2, "contact", "contact", "contact", "contact", "contact", BigDecimal.ZERO, null);

        //WHEN we add the contact
        existingUserAccount.addContact(contactToAdd);

        //THEN we get the contact in contactList
        assertEquals(contactToAdd, existingUserAccount.getContactList().get(0));
    }

    @Test
    public void addContactWhenHeIsAlreadyInTheContactListTest(){
        //GIVEN the contact to add is already in the contactList
        UserAccount existingUserAccount = new UserAccount(1, "test", "test", "test", "test", "test", BigDecimal.ZERO, new ArrayList<>());
        UserAccount contactToAdd = new UserAccount(2, "contact", "contact", "contact", "contact", "contact", BigDecimal.ZERO, null);
        existingUserAccount.getContactList().add(contactToAdd);

        //WHEN we try to add the contact THEN an exception is thrown
        assertThrows(RuntimeException.class, () -> existingUserAccount.addContact(contactToAdd));
    }
}
