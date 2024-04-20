package com.example.bankapplicatopm.dto.jar;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class CreateJarDTO {
    String jarName;
    BigDecimal maxSize;
    String currency;
    String comment;
}
