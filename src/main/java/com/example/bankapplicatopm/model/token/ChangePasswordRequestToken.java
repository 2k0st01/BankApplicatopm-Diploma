package com.example.bankapplicatopm.model.token;

import com.example.bankapplicatopm.model.BankAccount;
import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
public class ChangePasswordRequestToken {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "change_password_token_sequence"
    )
    @SequenceGenerator(
            name = "change_password_token_sequence",
            sequenceName = "change_password_token_sequence",
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

    public ChangePasswordRequestToken(String token,
                                  LocalDateTime createdAt,
                                  LocalDateTime expiresAt,
                                  BankAccount bankAccount) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.bankAccount = bankAccount;
    }
}
