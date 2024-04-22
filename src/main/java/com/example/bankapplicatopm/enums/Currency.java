package com.example.bankapplicatopm.enums;

public enum Currency {
    UAH,USD,EUR,CAD,CZK,HUF,CHF,AZN,PLN;

    @Override
    public String toString() {
        return this.name();
    }
}
