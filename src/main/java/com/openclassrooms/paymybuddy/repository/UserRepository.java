package com.openclassrooms.paymybuddy.repository;

import com.openclassrooms.paymybuddy.model.UserAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserAccount, Integer> {
    Optional<UserAccount> findByEmail(String email);
}
