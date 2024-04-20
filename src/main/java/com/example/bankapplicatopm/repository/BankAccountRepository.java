package com.example.bankapplicatopm.repository;

import com.example.bankapplicatopm.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

    Optional<BankAccount> findUserAccountByEmail(String email);

    BankAccount findByEmail(String email);

    BankAccount getUserAccountByEmail(String email);

    BankAccount findBankAccountByIBAN(String iban);

    BankAccount findBankAccountById(Long id);

    @Transactional
    @Modifying
    @Query("UPDATE BankAccount a " +
            "SET a.enabled = TRUE WHERE a.email = ?1")
    int enableAppUser(String email);
}
