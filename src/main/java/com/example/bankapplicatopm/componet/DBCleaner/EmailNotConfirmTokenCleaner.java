package com.example.bankapplicatopm.componet.DBCleaner;

import com.example.bankapplicatopm.model.token.EmailConfirmationToken;
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
public class EmailNotConfirmTokenCleaner {
    private final EmailConfirmationTokenService emailConfirmationTokenService;
    private final long TIME = 60;

    @Scheduled(fixedRate = 180_000_0) //every 30 min not confirmed tokens will be delete
    public void checkValidTokens() {
        List<EmailConfirmationToken> tokenList = emailConfirmationTokenService.findByConfirmedAtIsNull();

        if(tokenList != null) {
            for (EmailConfirmationToken token : tokenList) {

                Duration duration = Duration.between(token.getExpiresAt(), LocalDateTime.now());
                long differenceInMinutes = duration.toMinutes();

                if (TIME <= differenceInMinutes) {
                    emailConfirmationTokenService.deleteByToken(token.getToken());
                }
            }
        }
    }
}
