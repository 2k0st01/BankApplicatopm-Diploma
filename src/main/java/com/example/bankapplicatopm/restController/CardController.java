package com.example.bankapplicatopm.restController;

import com.example.bankapplicatopm.dto.cardDTO.CardDTO;
import com.example.bankapplicatopm.dto.cardDTO.CardSendDTO;
import com.example.bankapplicatopm.model.Card;
import com.example.bankapplicatopm.service.CardService;
import com.example.bankapplicatopm.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;

    @GetMapping("/api/card/get")
    public List<CardDTO> getMyCards(){
        List<Card> list = cardService.findCardByBankAccount();
        List<CardDTO> main = list.stream()
                .map(t -> {
                    CardDTO dto = new CardDTO();
                    dto.setDate(t.getDate());
                    dto.setCvv(t.getCvv());
                    dto.setNumber(t.getNumberCard());
                    dto.setCurrency(t.getWallet().getCurrency());
                    dto.setUserName(t.getUserName());
                    return dto;
                })
                .collect(Collectors.toList());
        return main;
    }

    @PostMapping("/api/card/create")
    public ResponseEntity<ApiResponse> cardCreate(@RequestBody String currency){
        try {
            cardService.createCard(currency);
            return ResponseEntity.ok(new ApiResponse(true, "Card was created", this));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false,
                    "Some error: " + e.getMessage()));
        }
    }

    @PostMapping("/api/card/send")
    public ResponseEntity<ApiResponse> sendMoney(@RequestBody CardSendDTO request){
        try {
            boolean status = cardService.sendMoneyByCards(request.getFromCard(), request.getToCard(),request.getSum(), request.getComment());
            return ResponseEntity.ok(new ApiResponse(status, "Transaction was made", this));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false,
                    "Some error: " + e.getMessage()));
        }
    }
}
