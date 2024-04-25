package com.example.bankapplicatopm.dto.cardDTO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CardSendDTO {
    private String fromCard;
    private String toCard;
    private BigDecimal sum;
    private String comment;
}
