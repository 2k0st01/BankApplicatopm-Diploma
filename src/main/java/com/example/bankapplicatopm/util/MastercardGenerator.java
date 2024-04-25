package com.example.bankapplicatopm.util;

import com.example.bankapplicatopm.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class MastercardGenerator {
    private final CardService cardService;
    private static final Random random = new Random();

    public static String generateMastercardNumber() {

        StringBuilder cardNumber = new StringBuilder("5");
        cardNumber.append(random.nextInt(5) + 1);

        for (int i = 0; i < 14; i++) {
            cardNumber.append(random.nextInt(10));
        }

        return cardNumber.toString();
    }

    public static String getCVV(){
        StringBuilder cardNumber = new StringBuilder();
        for (int i = 0; i < 3; i++) {
            cardNumber.append(random.nextInt(10));
        }
        return cardNumber.toString();
    }

    public static String getDate(){
        LocalDate localDate = LocalDate.now().plusYears(3);
        String date = localDate.toString();
        date = date.substring(2,7).replace("-", "/");
        return date;
    }
}

