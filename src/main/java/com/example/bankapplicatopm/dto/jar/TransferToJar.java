package com.example.bankapplicatopm.dto.jar;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferToJar {
    Long jarId;
    BigDecimal sum;
    String jarCurrency;
    String comment;

}
