package com.example.bankapplicatopm.service;

import com.example.bankapplicatopm.model.BankAccount;
import com.example.bankapplicatopm.model.token.EmailConfirmationToken;
import com.example.bankapplicatopm.repository.EmailConfirmationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EmailConfirmationTokenService {

    private final EmailConfirmationTokenRepository emailConfirmationTokenRepository;

    public void saveConfirmationToken(EmailConfirmationToken token) {
        emailConfirmationTokenRepository.save(token);
    }

    public Optional<EmailConfirmationToken> getToken(String token) {
        return emailConfirmationTokenRepository.findByToken(token);
    }

    public EmailConfirmationToken findConfirmationTokenByUserAccount(BankAccount bankAccount) {
        return emailConfirmationTokenRepository.findConfirmationTokenByBankAccount(bankAccount);
    }

    public int setConfirmedAt(String token) {
        return emailConfirmationTokenRepository.updateConfirmedAt(
                token, LocalDateTime.now());
    }
}
