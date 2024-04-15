package com.example.bankapplicatopm.service;

import com.example.bankapplicatopm.emailSender.EmailSender;
import com.example.bankapplicatopm.model.BankAccount;
import com.example.bankapplicatopm.model.token.EmailConfirmationToken;
import com.example.bankapplicatopm.repository.UserAccountRepository;
import com.example.bankapplicatopm.util.EmailBuilder;
import com.example.bankapplicatopm.util.IBANGenerated;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserAccountService implements UserDetailsService {
    private final EmailSender emailSender;
    private final UserAccountRepository userAccountRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EmailConfirmationTokenService emailConfirmationTokenService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userAccountRepository.findUserAccountByEmail(email).orElseThrow
                (() -> new UsernameNotFoundException("User with this email not found: " + email));
    }

    @Transactional
    public String singUpUser(BankAccount bankAccount) {
        boolean userExists = userAccountRepository.findUserAccountByEmail(bankAccount.getEmail()).isPresent();
        if (userExists) {
            BankAccount account = userAccountRepository.getUserAccountByEmail(bankAccount.getEmail());
            if (!account.isEnabled()) {
                EmailConfirmationToken emailConfirmationToken = emailConfirmationTokenService.findConfirmationTokenByUserAccount(account);
                if (emailConfirmationToken.getConfirmedAt() == null) {
                    String token = creatConfirmToken(account);
                    String link = "http://localhost:8080/api/registration/confirm?token=" + token;
                    emailSender.send(account.getEmail(), EmailBuilder.buildEmail(account.getFirstName(), link));
                    throw new IllegalStateException("User was not expired. We send new code on your Email address");
                }
            }
            throw new IllegalStateException("User already exist");
        }
        String encodePassword = bCryptPasswordEncoder.encode(bankAccount.getPassword());
        bankAccount.setPassword(encodePassword);

        userAccountRepository.save(bankAccount);
        bankAccount.setSpecialName("@user" + bankAccount.getId());
        bankAccount.setIBAN(IBANGenerated.generator(String.valueOf(bankAccount.getId())));
        userAccountRepository.save(bankAccount);

        return creatConfirmToken(bankAccount);
    }

    public String creatConfirmToken(BankAccount bankAccount) {
        String token = UUID.randomUUID().toString();
        EmailConfirmationToken emailConfirmationToken = new EmailConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                bankAccount
        );
        emailConfirmationTokenService.saveConfirmationToken(emailConfirmationToken);
        return token;
    }

    public BankAccount getUserAccountByEmail(String email){
        return userAccountRepository.getUserAccountByEmail(email);
    }

    public BankAccount findByEmail (String email){
        return userAccountRepository.findByEmail(email);
    }

    public int enableAppUser(String email) {
        return userAccountRepository.enableAppUser(email);
    }
}
