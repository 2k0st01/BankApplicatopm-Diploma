package com.example.bankapplicatopm.dto;

import com.example.bankapplicatopm.role.AccountType;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class AccountDTO {
    private String IBAN;
    private String specialName;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private LocalDate dateOfBirth;
    private String address;
    private AccountType accountType;
}
