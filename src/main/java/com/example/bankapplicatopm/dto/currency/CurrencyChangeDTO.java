package com.example.bankapplicatopm.dto.currency;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@ToString
public class CurrencyChangeDTO {
    private String from;
    private String to;
    private BigDecimal sum;
}
