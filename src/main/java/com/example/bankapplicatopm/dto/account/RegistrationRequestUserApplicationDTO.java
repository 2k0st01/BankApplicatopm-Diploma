package com.example.bankapplicatopm.dto.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class RegistrationRequestUserApplicationDTO {
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String password;
    private final String phone;
    private final LocalDate dateOfBirth;
}
