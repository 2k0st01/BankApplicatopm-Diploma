package com.example.bankapplicatopm.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
@Data

public class Wallet {

    @Id
    @GeneratedValue
    private long id;

    private String currency;

    private BigDecimal sum;

    @ManyToOne
    @JoinColumn(name = "account_id")
    @JsonBackReference
    private BankAccount account;

    @ManyToOne
    @JoinColumn(name = "jar_id", referencedColumnName = "id")
    private Jar jar;

    @Override
    public String toString() {
        return "Currency= " + currency + " : " + "Sum=" + sum;
    }

    public Wallet() {
        this.sum = new BigDecimal(0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Wallet wallet = (Wallet) o;
        return id == wallet.id && Objects.equals(currency, wallet.currency) && Objects.equals(sum, wallet.sum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, currency, sum);
    }
}
