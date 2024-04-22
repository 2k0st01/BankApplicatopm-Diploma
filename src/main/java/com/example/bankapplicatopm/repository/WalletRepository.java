package com.example.bankapplicatopm.repository;

import com.example.bankapplicatopm.model.BankAccount;
import com.example.bankapplicatopm.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WalletRepository extends JpaRepository<Wallet,Long> {
    List<Wallet> findWalletByAccountId(Long account_id);

    List<Wallet> findWalletByAccount(BankAccount account);

    Wallet findWalletByAccountIdAndCurrency(Long id, String currency);
}
