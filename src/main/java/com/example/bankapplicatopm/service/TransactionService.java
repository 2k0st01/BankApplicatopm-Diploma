package com.example.bankapplicatopm.service;


import com.example.bankapplicatopm.model.BankAccount;
import com.example.bankapplicatopm.model.Card;
import com.example.bankapplicatopm.model.Jar;
import com.example.bankapplicatopm.model.Transaction;
import com.example.bankapplicatopm.repository.TransactionRepository;
import com.example.bankapplicatopm.util.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final BankAccountService bankAccountService;

    @Transactional
    public Transaction addTransaction(String forAccount, BigDecimal sum, String comment, String currency){
        BankAccount bankAccount = CurrentUser.getCurrentUser();
        String fromAccountEmail = bankAccount.getEmail();

        if(bankAccountService.sendMoney(sum,forAccount,currency)){
            BankAccount account1 = bankAccountService.findByEmail(fromAccountEmail);
            BankAccount account2 = bankAccountService.findBankAccountByIBAN(forAccount);
            Transaction transaction = new Transaction();

            transaction.setToAccount(
                    account2.getFirstName() + " " + account2.getLastName()
            );
            transaction.setFromAccount(
                    account1.getFirstName() + " " + account1.getLastName()
            );

            transaction.setSum(sum);
            transaction.setComment(comment);
            transaction.setDate(LocalDateTime.now());
            transaction.setAccount(new ArrayList<>(Arrays.asList(account1, account2)));
            transaction.setCurrency(currency);
            return transactionRepository.save(transaction);
        } else throw  new IllegalStateException("Error");
    }

    @Transactional
    public void addJarTransaction(BankAccount bankAccount, Jar jar, BigDecimal sum, String currency, String comment, String to){
        Transaction transaction = new Transaction();
        transaction.setToAccount(to);
        transaction.setJar(jar);
        transaction.setFromAccount(
                bankAccount.getFirstName() + " " + bankAccount.getLastName()
        );
        transaction.setSum(sum);
        transaction.setDate(LocalDateTime.now());
        transaction.setComment(comment);
        transaction.setAccount(new ArrayList<>(Arrays.asList(bankAccount)));
        transaction.setCurrency(currency);
        transactionRepository.save(transaction);
    }

    @Transactional
    public void addCardTransaction(Card fromCard, Card toCard, BigDecimal sum, String comment){
        Transaction transaction = new Transaction();
        transaction.setFromAccount(fromCard.getUserName());
        transaction.setToAccount(toCard.getUserName());

        transaction.setCard(new ArrayList<>(Arrays.asList(toCard,fromCard)));
        transaction.setAccount(new ArrayList<>(Arrays.asList(toCard.getBankAccount(), fromCard.getBankAccount())));
        transaction.setSum(sum);
        transaction.setDate(LocalDateTime.now());
        transaction.setComment(comment);
        transaction.setCurrency(fromCard.getWallet().getCurrency());

        transactionRepository.save(transaction);
    }

    @Transactional
    public List<Transaction> getJarTransaction(Long id){
        return transactionRepository.getTransactionByJarId(id);
    }

    @Transactional
    public void closeJar(BankAccount bankAccount, Jar jar, BigDecimal sum, String currency, String comment){
        Transaction transaction = new Transaction();
        transaction.setFromAccount(jar.getJarName());
        transaction.setSum(sum);
        transaction.setComment(comment);
        transaction.setAccount(new ArrayList<>(Arrays.asList(bankAccount)));
        transaction.setCurrency(currency);
        transactionRepository.save(transaction);
        transactionRepository.deleteTransactionByJar(jar);
    }

    public Page<Transaction> getUserTransactions(Long accountId, Pageable pageable) {
        return transactionRepository.findTransactionByAccountId(accountId, pageable);
    }

    @Transactional
    public List<Transaction> getUserTransactions(Long userId) {
        return transactionRepository.findTransactionByAccountId(userId);
    }

}
