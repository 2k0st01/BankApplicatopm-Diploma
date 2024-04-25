package com.example.bankapplicatopm.componet.DBCleaner;

import com.example.bankapplicatopm.model.Card;
import com.example.bankapplicatopm.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;


@Component
@RequiredArgsConstructor
public class CheckCartDate {
    private final CardService cardService;


    @Scheduled(cron = "0 0 0 * * ?") // start check every-day at 00:00
    public void checkValidCard() {
        LocalDate localDate = LocalDate.now();
        String date = localDate.toString();
        date = date.substring(2, 7).replace("-", "/");
        List<Card> list = cardService.findCardByDate(date);

        if (list != null) {
            for (Card card : list) {
                card.setEnable(false);
                cardService.save(card);
            }
        }
    }
}
