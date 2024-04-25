package com.example.bankapplicatopm.dto.account;

import com.example.bankapplicatopm.enums.UserRole;
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
    private UserRole userRole;

}
