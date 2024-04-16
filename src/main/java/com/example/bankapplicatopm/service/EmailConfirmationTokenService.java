package com.example.bankapplicatopm.service;

import com.example.bankapplicatopm.model.BankAccount;
import com.example.bankapplicatopm.model.token.EmailConfirmationToken;
import com.example.bankapplicatopm.repository.EmailConfirmationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EmailConfirmationTokenService {

    private final EmailConfirmationTokenRepository emailConfirmationTokenRepository;

    public void saveConfirmationToken(EmailConfirmationToken token) {
        emailConfirmationTokenRepository.save(token);
    }

    @Transactional
    public Optional<EmailConfirmationToken> getToken(String token) {
        return emailConfirmationTokenRepository.findByToken(token);
    }

    @Transactional
    public EmailConfirmationToken findConfirmationTokenByUserAccount(BankAccount bankAccount) {
        return emailConfirmationTokenRepository.findConfirmationTokenByBankAccount(bankAccount);
    }

    @Transactional
    public List<EmailConfirmationToken> findByConfirmedAtIsNull() {
        return emailConfirmationTokenRepository.findByConfirmedAtIsNull();
    }

    @Transactional
    public void deleteByToken(String token){
        emailConfirmationTokenRepository.deleteByToken(token);
    }

    @Transactional
    public int setConfirmedAt(String token) {
        return emailConfirmationTokenRepository.updateConfirmedAt(
                token, LocalDateTime.now());
    }
}
