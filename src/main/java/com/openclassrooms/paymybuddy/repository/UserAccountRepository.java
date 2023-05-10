package com.openclassrooms.paymybuddy.repository;

import com.openclassrooms.paymybuddy.model.UserAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserAccountRepository extends CrudRepository<UserAccount, Integer> {
    /**
     * find a user from its email
     * @param email the email of the user
     * @return an optional of a UserAccount
     */
    Optional<UserAccount> findByEmail(String email);
}
