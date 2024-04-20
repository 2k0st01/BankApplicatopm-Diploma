package com.example.bankapplicatopm.service;

import com.example.bankapplicatopm.dto.account.RegistrationRequestUserApplicationDTO;
import com.example.bankapplicatopm.emailSender.EmailSender;
import com.example.bankapplicatopm.model.BankAccount;
import com.example.bankapplicatopm.model.token.EmailConfirmationToken;
import com.example.bankapplicatopm.enums.AccountType;
import com.example.bankapplicatopm.enums.UserRole;
import com.example.bankapplicatopm.componet.validator.EmailValidator;
import com.example.bankapplicatopm.componet.validator.PhoneValidator;
import com.example.bankapplicatopm.util.EmailBuilder;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {
    private final EmailValidator emailValidator;
    private final PhoneValidator phoneValidator;
    private final BankAccountService bankAccountService;
    private final EmailConfirmationTokenService emailConfirmationTokenService;
    private final EmailSender emailSender;


    @Transactional
    public BankAccount register(RegistrationRequestUserApplicationDTO request) {
        boolean isValidEmail = emailValidator.test(request.getEmail());
        boolean isValidPhone = phoneValidator.test(request.getPhone());
        String mobilePhone = "+380" + request.getPhone();
        if(!isValidEmail){
            throw new IllegalStateException("Email is not valid");
        }
        if(!isValidPhone){
            throw new IllegalStateException("Phone is not valid");
        }

        BankAccount bankAccount;
        String token = bankAccountService.singUpUser(bankAccount = new BankAccount(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPassword(),
                mobilePhone,
                request.getDateOfBirth(),
                UserRole.ROLE_USER,
                AccountType.STANDARD
        ));
        String link = "http://localhost:8080/registration/confirm?token=" + token;
        emailSender.send(request.getEmail(), EmailBuilder.confirmYourEmail(request.getFirstName(), link) );
        return bankAccount;
    }

    @Transactional
    public String confirmToken(String token) {
        EmailConfirmationToken emailConfirmationToken = emailConfirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token not found"));

        if (emailConfirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = emailConfirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        emailConfirmationTokenService.setConfirmedAt(token);
        bankAccountService.enableAppUser(
                emailConfirmationToken.getBankAccount().getEmail());
        return "confirmed";
    }



}
