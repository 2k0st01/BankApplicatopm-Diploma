package com.example.bankapplicatopm.util;

import com.example.bankapplicatopm.model.BankAccount;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class CurrentUser {

    public static BankAccount getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof BankAccount) {
            return (BankAccount) authentication.getPrincipal();
        }
        throw new IllegalStateException("No authenticated user found");
    }

}
