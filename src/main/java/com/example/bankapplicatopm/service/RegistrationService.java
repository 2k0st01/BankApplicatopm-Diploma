package com.example.bankapplicatopm.service;

import com.example.bankapplicatopm.componet.AuthenticationResponse;
import com.example.bankapplicatopm.dto.account.AuthRequest;
import com.example.bankapplicatopm.dto.account.RegistrationRequestUserApplicationDTO;
import com.example.bankapplicatopm.emailSender.EmailSender;
import com.example.bankapplicatopm.model.BankAccount;
import com.example.bankapplicatopm.model.token.EmailConfirmationToken;
import com.example.bankapplicatopm.enums.UserRole;
import com.example.bankapplicatopm.componet.validator.EmailValidator;
import com.example.bankapplicatopm.componet.validator.PhoneValidator;
import com.example.bankapplicatopm.util.EmailBuilder;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RegistrationService {
    private final EmailValidator emailValidator;
    private final PhoneValidator phoneValidator;
    private final BankAccountService bankAccountService;
    private final EmailConfirmationTokenService emailConfirmationTokenService;
    private final EmailSender emailSender;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    @Transactional
    public AuthenticationResponse register(RegistrationRequestUserApplicationDTO request) {
        if(!emailValidator.test(request.getEmail())){
            throw new IllegalStateException("Email is not valid");
        }
        if(!phoneValidator.test(request.getPhone())){
            throw new IllegalStateException("Phone is not valid");
        }

        String mobilePhone = "+380" + request.getPhone();

        BankAccount bankAccount;
        String token = bankAccountService.singUpUser(bankAccount = new BankAccount(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPassword(),
                mobilePhone,
                request.getDateOfBirth(),
                UserRole.ROLE_USER
        ));

        String link = "https://kosto-app-bank-53a05f6291cb.herokuapp.com/confirm?token=" + token;
        emailSender.send(request.getEmail(), EmailBuilder.confirmYourEmail(request.getFirstName(), link) );
        var jwtToken = jwtService.generateToken(bankAccount);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
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


    public AuthenticationResponse auth(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        var bankAccount = bankAccountService.findUserAccountByEmail(request.getUsername())
                .orElseThrow();

        var jwtToken = jwtService.generateToken(bankAccount);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
