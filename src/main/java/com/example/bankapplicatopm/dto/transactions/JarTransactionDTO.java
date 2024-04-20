package com.example.bankapplicatopm.dto.transactions;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor
public class JarTransactionDTO {
    private String fromAccount;
    private LocalDateTime date;
    private String comment;
    private BigDecimal sum;
}
