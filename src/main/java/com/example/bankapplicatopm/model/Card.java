package com.example.bankapplicatopm.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "card_seq_gen")
    @SequenceGenerator(name = "card_seq_gen", sequenceName = "card_seq", allocationSize = 1)
    private Long id;
    @Column(nullable = false)
    private String numberCard;

    private boolean enable = true;

    @ManyToOne
    @JoinColumn(name = "wallet_id", referencedColumnName = "id")
    private Wallet wallet;

    @Column (nullable = false)
    private String userName;

    @Column(nullable = false)
    private String date;
    @Column(nullable = false)
    private String cvv;

    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private BankAccount bankAccount;
}
