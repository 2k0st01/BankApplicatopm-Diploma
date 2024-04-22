package com.example.bankapplicatopm.service;

import com.example.bankapplicatopm.dto.wallet.WalletDTO;
import com.example.bankapplicatopm.enums.Currency;
import com.example.bankapplicatopm.model.BankAccount;
import com.example.bankapplicatopm.model.Wallet;
import com.example.bankapplicatopm.repository.WalletRepository;
import com.example.bankapplicatopm.util.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final BankAccountService bankAccountService;


    @Transactional
    public List<WalletDTO> findWalletByAccountId() {
        BankAccount bankAccount = CurrentUser.getCurrentUser();
        List<Wallet> wallets = walletRepository.findWalletByAccountId(bankAccount.getId());
        List<WalletDTO> list = new ArrayList<>();
        for(Wallet wallet: wallets){
            WalletDTO walletDTO = new WalletDTO();
            walletDTO.setCurrency(wallet.getCurrency());
            walletDTO.setSum(wallet.getSum());
            list.add(walletDTO);
        }
        return list;
    }

    @Transactional
    public void save(Wallet wallet) {
        walletRepository.save(wallet);
    }

    @Transactional
    public Wallet findWalletByAccountIdAndCurrency(Long id, String currency){
        return walletRepository.findWalletByAccountIdAndCurrency(id,currency);
    }

    @Transactional
    public Wallet createWallet(String currency) {
        for(Currency c : Currency.values()){
            if(c.toString().equals(currency)){
                BankAccount bankAccount = CurrentUser.getCurrentUser();
                for (Wallet w : walletRepository.findWalletByAccount(bankAccount)) {
                    if (w.getCurrency().equals(currency)) {
                        throw new IllegalStateException("You already have this currency: " + currency);
                    }
                }
                Wallet wallet = new Wallet();
                wallet.setCurrency(currency);
                wallet.setAccount(bankAccount);
                wallet.setCurrency(currency);
                return walletRepository.save(wallet);
            }
        }
        throw new IllegalStateException("We can't create for you wallet account like this: " + currency);
    }

    @Transactional
    public Wallet createWalletForSendBankAccount(Long id, String currency){
        for(Currency c : Currency.values()){
            if(c.toString().equals(currency)){
                BankAccount bankAccount = bankAccountService.findBankAccountById(id);
                Wallet wallet = new Wallet();
                wallet.setAccount(bankAccount);
                wallet.setCurrency(currency);
                return walletRepository.save(wallet);
            }
        }
        throw new IllegalStateException("We can't create for you wallet account like this: " + currency);
    }

    @Transactional
    public Wallet createBaseWallet(BankAccount account, String currency) {
        Wallet wallet = new Wallet();
        wallet.setAccount(account);
        wallet.setCurrency(currency);
        wallet.setSum(BigDecimal.valueOf(3003));
        return walletRepository.save(wallet);
    }

}
