package com.example.bankapplicatopm.model;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_seq_gen")
    @SequenceGenerator(name = "transaction_seq_gen", sequenceName = "transaction_seq", allocationSize = 1)
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
    @JoinColumn(name = "card_id", referencedColumnName = "id")
    private List<Card> card;

    @ManyToMany
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private List<BankAccount> account;
}