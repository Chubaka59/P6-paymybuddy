package com.openclassrooms.paymybuddy.repository;

import com.openclassrooms.paymybuddy.model.Transaction;
import com.openclassrooms.paymybuddy.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    List<Transaction> findAllByDebtorOrCreditorOrderByDateAsc(UserAccount debtor, UserAccount creditor);

//    @Query("select t from Transaction t where t.creditor = :user or t.debtor = :user order by t.date desc")
//    List<Transaction> findAllUserTransactions(@Param("user") UserAccount user);
}
