package com.example.bankapplicatopm.model;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Jar {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "jar_seq_gen")
    @SequenceGenerator(name = "jar_seq_gen", sequenceName = "jar_seq", allocationSize = 1)
    private Long id;

    private boolean bankStatus = false;

    @Column(nullable = false)
    private String jarName;

    private BigDecimal minSize;
    @Column(nullable = false)
    private BigDecimal maxSize;
    private BigDecimal currentSum;

    @Column(nullable = false)
    private String comment;
    @Column(nullable = false)
    private String currency;

    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private BankAccount bankAccount;

    @OneToMany(mappedBy = "jar")
    private List<Transaction> transaction;
}
