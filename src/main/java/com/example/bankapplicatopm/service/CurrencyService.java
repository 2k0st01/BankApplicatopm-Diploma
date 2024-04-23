package com.example.bankapplicatopm.service;

import com.example.bankapplicatopm.model.BankAccount;
import com.example.bankapplicatopm.model.CurrencyModel;
import com.example.bankapplicatopm.model.Wallet;
import com.example.bankapplicatopm.repository.CurrencyRepository;
import com.example.bankapplicatopm.util.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrencyService {
    private final CurrencyRepository currencyRepository;
    private final WalletService walletService;

    @Transactional
    public void save(CurrencyModel currencyModel) {
        currencyRepository.save(currencyModel);
    }

    @Transactional
    public CurrencyModel findCurrencyModelByCc(String cc) {
        return currencyRepository.findCurrencyModelByCc(cc);
    }

    @Transactional
    public List<CurrencyModel> getAllCurrency() {
        return currencyRepository.getAll();
    }

    @Transactional
    public void changeCurrency(String from, String to, BigDecimal sum) {
        if(from.equals(to)){
            throw new IllegalStateException("You true use same wallets: " + from);
        }

        BankAccount bankAccount = CurrentUser.getCurrentUser();
        Wallet walletFrom = walletService.findWalletByAccountIdAndCurrency(bankAccount.getId(), from);
        Wallet walletTo = walletService.findWalletByAccountIdAndCurrency(bankAccount.getId(), to);
        CurrencyModel findFrom = currencyRepository.findCurrencyModelByCc(from);
        CurrencyModel findTo = currencyRepository.findCurrencyModelByCc(to);

        if (walletFrom == null) {
            throw new IllegalStateException("You don't have wallet: " + from);
        }

        if (walletTo == null) {
            walletTo = walletService.createWallet(to);
            walletService.save(walletTo);
        }

        if (walletFrom.getSum().compareTo(sum) < 0) {
            throw new IllegalStateException("Not enough money on balance");
        }

        if (findFrom == null && findTo == null) {
            throw new IllegalStateException("Some think wrong");
        }

        performTransaction(walletFrom,walletTo,findFrom,findTo,sum);
    }

    private void performTransaction(Wallet from, Wallet to, CurrencyModel currencyFrom, CurrencyModel currencyTo, BigDecimal amount) {
        BigDecimal amountToTransfer = calculateAmount(currencyFrom, currencyTo, amount);
        from.setSum(from.getSum().subtract(amount));
        to.setSum(to.getSum().add(amountToTransfer));
        walletService.save(from);
        walletService.save(to);
    }

    private BigDecimal calculateAmount(CurrencyModel from, CurrencyModel to, BigDecimal amount) {
        if (from == null && !to.getCc().equals("UAH")) {
            return amount.divide(to.getRate(), 2, RoundingMode.HALF_EVEN);
        } else if (to == null && !from.getCc().equals("UAH")) {
            return from.getRate().multiply(amount);
        } else {
            BigDecimal amountToUAH = from.getRate().multiply(amount);
            return amountToUAH.divide(to.getRate(), 2, RoundingMode.HALF_EVEN);
        }
    }
}
