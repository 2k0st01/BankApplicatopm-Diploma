package com.example.bankapplicatopm.model.token;

import com.example.bankapplicatopm.model.BankAccount;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
public class EmailConfirmationToken {

    @Id
    @GeneratedValue (
            strategy = GenerationType.SEQUENCE,
            generator = "confirm_token_sequence"
    )
    @SequenceGenerator(
            name = "confirm_token_sequence",
            sequenceName = "confirm_token_sequence",
            allocationSize = 1
    )
    private Long id;
    @Column(nullable = false)
    private String token;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime expiresAt;
    private LocalDateTime confirmedAt;

    @ManyToOne
    @JoinColumn(nullable = false, name = "bank_account_id")
    private BankAccount bankAccount;

    public EmailConfirmationToken(String token,
                                  LocalDateTime createdAt,
                                  LocalDateTime expiresAt,
                                  BankAccount bankAccount) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.bankAccount = bankAccount;
    }
}
