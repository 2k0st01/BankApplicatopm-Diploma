package com.example.bankapplicatopm.repository;

import com.example.bankapplicatopm.model.BankAccount;
import com.example.bankapplicatopm.model.token.EmailConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface EmailConfirmationTokenRepository extends JpaRepository<EmailConfirmationToken, Long> {

    EmailConfirmationToken findConfirmationTokenByToken(String token);

    EmailConfirmationToken findConfirmationTokenByBankAccount(BankAccount bankAccount);

    Optional<EmailConfirmationToken> findByToken(String token);

    @Transactional
    @Modifying
    @Query("UPDATE EmailConfirmationToken c " +
            "SET c.confirmedAt = ?2 " +
            "WHERE c.token = ?1")
    int updateConfirmedAt(String token,
                          LocalDateTime confirmedAt);
}
