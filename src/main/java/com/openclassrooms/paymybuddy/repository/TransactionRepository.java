package com.openclassrooms.paymybuddy.repository;

import com.openclassrooms.paymybuddy.model.Transaction;
import com.openclassrooms.paymybuddy.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer>, PagingAndSortingRepository<Transaction, Integer> {
    /**
     * find a list of transaction from the specified users
     * @param debtor the user to find the transaction
     * @param creditor the user to find the transaction
     * @return a list of transaction
     */
    List<Transaction> findAllByDebtorOrCreditorOrderByDateDesc(UserAccount debtor, UserAccount creditor);
}
