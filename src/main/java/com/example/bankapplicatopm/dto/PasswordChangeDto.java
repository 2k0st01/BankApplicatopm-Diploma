package com.example.bankapplicatopm.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PasswordChangeDto {
    private String newPassword;
    private String confirmPassword;
}
