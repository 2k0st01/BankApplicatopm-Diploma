package com.example.bankapplicatopm.dto.cardDTO;

import lombok.Data;

@Data
public class CardDTO {
    private String number;
    private String date;
    private String cvv;
    private String currency;
    private String userName;
}
