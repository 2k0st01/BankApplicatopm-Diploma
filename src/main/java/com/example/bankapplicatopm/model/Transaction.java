package com.example.bankapplicatopm.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue
    private Long id;

    private String toAccount;

    private String fromAccount;

    private LocalDateTime date;

    private String comment;

    private BigDecimal sum;

    private String currency;

    @ManyToOne
    @JoinColumn(name = "jar_id", referencedColumnName = "id")
    private Jar jar;

    @ManyToMany
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private List<BankAccount> account;
}