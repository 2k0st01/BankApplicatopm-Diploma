package com.example.bankapplicatopm.service;

import com.example.bankapplicatopm.dto.RegistrationRequestUserApplicationDTO;
import com.example.bankapplicatopm.emailSender.EmailSender;
import com.example.bankapplicatopm.model.BankAccount;
import com.example.bankapplicatopm.model.token.EmailConfirmationToken;
import com.example.bankapplicatopm.role.AccountType;
import com.example.bankapplicatopm.role.UserRole;
import com.example.bankapplicatopm.service.validator.EmailValidator;
import com.example.bankapplicatopm.service.validator.PhoneValidator;
import com.example.bankapplicatopm.util.EmailBuilder;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {
    private final EmailValidator emailValidator;
    private final PhoneValidator phoneValidator;
    private final UserAccountService userAccountService;
    private final EmailConfirmationTokenService emailConfirmationTokenService;
    private final EmailSender emailSender;

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
        String token = userAccountService.singUpUser(bankAccount = new BankAccount(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPassword(),
                mobilePhone,
                request.getDateOfBirth(),
                UserRole.USER,
                AccountType.STANDARD
        ));
        String link = "http://localhost:8080/api/registration/confirm?token=" + token;
        emailSender.send(request.getEmail(), EmailBuilder.buildEmail(request.getFirstName(), link) );
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
        userAccountService.enableAppUser(
                emailConfirmationToken.getBankAccount().getEmail());
        return "confirmed";
    }


}
