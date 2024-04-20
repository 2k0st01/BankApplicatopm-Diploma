package com.example.bankapplicatopm.service;

import com.example.bankapplicatopm.model.BankAccount;
import com.example.bankapplicatopm.model.Jar;
import com.example.bankapplicatopm.model.Transaction;
import com.example.bankapplicatopm.model.Wallet;
import com.example.bankapplicatopm.repository.JarRepository;
import com.example.bankapplicatopm.util.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JarService {
    private final JarRepository jarRepository;
    private final TransactionService transactionService;
    private final WalletService walletService;

    @Transactional
    public Long createJar(String jarName,BigDecimal maxSize, String currency, String comment){
        BankAccount bankAccount = CurrentUser.getCurrentUser();
        Jar jar = new Jar();

        jar.setBankAccount(bankAccount);
        jar.setJarName(jarName);
        jar.setBankStatus(true);
        jar.setMinSize(new BigDecimal(0));
        jar.setMaxSize(maxSize);
        jar.setComment(comment);
        jar.setCurrency(currency);
        jar.setCurrentSum(new BigDecimal(0));
        Jar jar1 = jarRepository.save(jar);
        return jar1.getId();
    }

    @Transactional
    public boolean closeJar(Long jarId){
        BankAccount bankAccount = CurrentUser.getCurrentUser();
        Jar jar = findJarById(jarId);

        Wallet accountWallet = walletService.findWalletByAccountIdAndCurrency(bankAccount.getId(), jar.getCurrency());

        if (accountWallet == null) {
            accountWallet = walletService.createWallet(jar.getCurrency());
        }

        accountWallet.setSum(accountWallet.getSum().add(jar.getCurrentSum()));

        walletService.save(accountWallet);
        transactionService.closeJar(bankAccount,jar,jar.getCurrentSum(),jar.getCurrency(),"From Jar:" + jar.getJarName());
        jarRepository.deleteById(jar.getId());
        return true;
    }

    @Transactional
    public boolean transferMoney(Long jarId,BigDecimal sum,String jarCurrency,String comment){
        BankAccount bankAccount = CurrentUser.getCurrentUser();
        Jar jar = findJarById(jarId);
        if(!jar.isBankStatus()){
            return false;
        }

        Wallet fromWallet = walletService.findWalletByAccountIdAndCurrency(bankAccount.getId(), jarCurrency);
        if(fromWallet == null){
            return false;
        }

        if(!jar.getCurrency().equals(jarCurrency)){
            return false;
        }

        if (fromWallet.getSum().compareTo(sum) < 0) {
            return false;
        }

        transactionService.addJarTransaction(bankAccount,jar,sum,jarCurrency,comment,jar.getJarName());

        fromWallet.setSum(fromWallet.getSum().subtract(sum));
        jar.setCurrentSum(jar.getCurrentSum().add(sum));

        walletService.save(fromWallet);
        jarRepository.save(jar);

        if(jar.getCurrentSum().compareTo(jar.getMaxSize()) > 0){
            jar.setBankStatus(false);
            jar.setComment("Jar was closed!!! Thank you for every one.");
            jarRepository.save(jar);
            return true;
        }
        return true;
    }

    public List<Transaction> getJarTransaction(Long id){
        return transactionService.getJarTransaction(id);
    }


    @Transactional
    public Jar findJarById(Long id){
        return jarRepository.findJarById(id);
    }
}
