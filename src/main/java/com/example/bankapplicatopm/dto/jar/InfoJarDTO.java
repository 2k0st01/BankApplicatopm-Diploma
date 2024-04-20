package com.example.bankapplicatopm.dto.jar;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
public class InfoJarDTO {
    private Long id;
    private String jarName;
    private BigDecimal maxSize;
    private BigDecimal currentSum;
    private String comment;
    private String currency;
}
