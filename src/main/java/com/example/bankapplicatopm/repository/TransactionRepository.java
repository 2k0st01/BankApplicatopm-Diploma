package com.example.bankapplicatopm.repository;

import com.example.bankapplicatopm.model.Jar;
import com.example.bankapplicatopm.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Long> {

    List<Transaction> findTransactionByAccountId(Long id);

    Page<Transaction> findTransactionByAccountId(Long id, Pageable pageable);

    List<Transaction> getTransactionByFromAccount(String admin);

    List<Transaction> getTransactionByJarId(Long id);

    void deleteTransactionByJar(Jar jar);

}
