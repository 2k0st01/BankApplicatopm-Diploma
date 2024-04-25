package com.example.bankapplicatopm.repository;

import com.example.bankapplicatopm.model.BankAccount;
import com.example.bankapplicatopm.model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    Card findCardByNumberCard(String number);

    List<Card> findCardByBankAccount(BankAccount bankAccount);

    List<Card> findCardByDate(String date);
}
