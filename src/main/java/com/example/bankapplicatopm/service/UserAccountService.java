package com.example.bankapplicatopm.service;

import com.example.bankapplicatopm.dto.EmailDTO;
import com.example.bankapplicatopm.emailSender.EmailSender;
import com.example.bankapplicatopm.model.BankAccount;
import com.example.bankapplicatopm.model.token.ChangePasswordRequestToken;
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
    private final ChangePasswordRequestTokenService changePasswordRequestTokenService;

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
                String link = "http://localhost:8080/api/registration/confirm?token=";
                if (emailConfirmationToken == null) {
                    String token = creatConfirmToken(account);
                    emailSender.send(account.getEmail(), EmailBuilder.confirmYourEmail(account.getFirstName(), link + token));
                    throw new IllegalStateException("User was not expired. We send new code on your Email address");
                } else if (emailConfirmationToken.getConfirmedAt() == null) {
                    emailSender.send(account.getEmail(), EmailBuilder.confirmYourEmail(account.getFirstName(), link + emailConfirmationToken.getToken()));
                    throw new IllegalStateException("User was not expired. We re-send your code on your Email address");
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

    @Transactional
    public BankAccount forgotPassword(String email) {
        String link = "http://localhost:8080/change-password?token=";
        BankAccount bankAccount = userAccountRepository.findByEmail(email);
        if (bankAccount != null) {
            String token = creatChangeForgotPasswordToken(bankAccount);
            emailSender.send(bankAccount.getEmail(), EmailBuilder.requestForChanePassword(bankAccount.getFirstName(), link + token));
        } else
            throw new IllegalStateException("User not found with this email: " + email);

        return bankAccount;
    }

    @Transactional
    public void changeForgetPasswordByToken(String token, String newPassword) {
        ChangePasswordRequestToken changePasswordRequestToken =
                changePasswordRequestTokenService.findChangePasswordRequestTokenByToken(token);
        BankAccount bankAccount = changePasswordRequestToken.getBankAccount();
        if(bankAccount != null){
            String encodePassword = bCryptPasswordEncoder.encode(newPassword);
            bankAccount.setPassword(encodePassword);
            userAccountRepository.save(bankAccount);
            changePasswordRequestTokenService.deleteChangePasswordRequestTokenByToken(token);
        } else
            throw new IllegalStateException("Bank account was not find");
    }

    @Transactional
    public BankAccount getUserAccountByEmail(String email) {
        return userAccountRepository.getUserAccountByEmail(email);
    }

    @Transactional
    public BankAccount findByEmail(String email) {
        return userAccountRepository.findByEmail(email);
    }

    @Transactional
    public void enableAppUser(String email) {
        userAccountRepository.enableAppUser(email);
    }


    private String creatChangeForgotPasswordToken(BankAccount bankAccount) {
        String token = UUID.randomUUID().toString();
        ChangePasswordRequestToken changePasswordRequestToken = new ChangePasswordRequestToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(5),
                bankAccount
        );
        changePasswordRequestTokenService.save(changePasswordRequestToken);
        return token;
    }

    private String creatConfirmToken(BankAccount bankAccount) {
        String token = UUID.randomUUID().toString();
        EmailConfirmationToken emailConfirmationToken = new EmailConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(60),
                bankAccount
        );
        emailConfirmationTokenService.saveConfirmationToken(emailConfirmationToken);
        return token;
    }
}
