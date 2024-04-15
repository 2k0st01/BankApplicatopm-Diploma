package com.example.bankapplicatopm.role;

public enum UserRole {
    USER, ADMIN;

    @Override
    public String toString() {
        return "ROLE_" + name();
    }
}
