package com.example.bankapplicatopm.dto.transactions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class TransactionDTO {
    private BigDecimal sum;
    private String comment;
    private String toAccount;
    private String currency;
}
