package com.example.bankapplicatopm.componet.DBCleaner;

import com.example.bankapplicatopm.model.token.ChangePasswordRequestToken;
import com.example.bankapplicatopm.model.token.EmailConfirmationToken;
import com.example.bankapplicatopm.service.ChangePasswordRequestTokenService;
import com.example.bankapplicatopm.service.EmailConfirmationTokenService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@Component
public class ForgotPasswordNotConfirmTokenCleaner {
    private final ChangePasswordRequestTokenService changePasswordRequestTokenService;
    private final long TIME = 5;

    @Scheduled(fixedRate = 360_000_0) // every 60 min not confirmed tokens will be delete
    public void checkValidTokens() {
        List<ChangePasswordRequestToken> tokenList = changePasswordRequestTokenService.findByConfirmedAtIsNull();

        if(tokenList != null) {
            for (ChangePasswordRequestToken token : tokenList) {

                Duration duration = Duration.between(token.getExpiresAt(), LocalDateTime.now());
                long differenceInMinutes = duration.toMinutes();

                if (TIME <= differenceInMinutes) {
                    changePasswordRequestTokenService.deleteChangePasswordRequestTokenByToken(token.getToken());
                }
            }
        }
    }
}
