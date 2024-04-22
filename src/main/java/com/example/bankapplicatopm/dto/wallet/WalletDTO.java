package com.example.bankapplicatopm.dto.wallet;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class WalletDTO {
    private String currency;
    private BigDecimal sum;
}
