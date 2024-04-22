package com.example.bankapplicatopm.dto.currency;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CurrencyDTO {
    private String cc;
    private BigDecimal rate;
}
