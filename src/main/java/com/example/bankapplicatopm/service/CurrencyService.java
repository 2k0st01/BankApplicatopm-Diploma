package com.example.bankapplicatopm.service;

import com.example.bankapplicatopm.model.BankAccount;
import com.example.bankapplicatopm.model.CurrencyRate;
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
    public void save(CurrencyRate currencyRate) {
        currencyRepository.save(currencyRate);
    }

    @Transactional
    public CurrencyRate findCurrencyModelByCc(String cc) {
        return currencyRepository.findCurrencyModelByCc(cc);
    }

    @Transactional
    public List<CurrencyRate> getAllCurrency() {
        return currencyRepository.getAll();
    }

    @Transactional
    public void changeCurrency(String from, String to, BigDecimal sum) {
        if(from.equals(to)){
            throw new IllegalStateException("You true use same wallets: " + from);
        }
        BigDecimal deductedAmount = interestDeduction(sum);
        BankAccount bankAccount = CurrentUser.getCurrentUser();
        Wallet walletFrom = walletService.findWalletByAccountIdAndCurrency(bankAccount.getId(), from);
        Wallet walletTo = walletService.findWalletByAccountIdAndCurrency(bankAccount.getId(), to);
        CurrencyRate findFrom = currencyRepository.findCurrencyModelByCc(from);
        CurrencyRate findTo = currencyRepository.findCurrencyModelByCc(to);

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

        performTransaction(walletFrom,walletTo,findFrom,findTo,deductedAmount,sum);
    }

    private void performTransaction(Wallet from, Wallet to, CurrencyRate currencyFrom, CurrencyRate currencyTo, BigDecimal deductedAmount , BigDecimal amount) {
        BigDecimal amountToTransfer = calculateAmount(currencyFrom, currencyTo, deductedAmount);
        from.setSum(from.getSum().subtract(amount));
        to.setSum(to.getSum().add(amountToTransfer));
        walletService.save(from);
        walletService.save(to);
    }

    private BigDecimal calculateAmount(CurrencyRate from, CurrencyRate to, BigDecimal amount) {
        if (from == null && !to.getCc().equals("UAH")) {
            return amount.divide(to.getRate(), 2, RoundingMode.HALF_EVEN);
        } else if (to == null && !from.getCc().equals("UAH")) {
            return from.getRate().multiply(amount);
        } else {
            BigDecimal amountToUAH = from.getRate().multiply(amount);
            return amountToUAH.divide(to.getRate(), 2, RoundingMode.HALF_EVEN);
        }
    }

    private BigDecimal interestDeduction(BigDecimal sum){
        BigDecimal percent = new BigDecimal("2");
        BigDecimal hundred = new BigDecimal("100");
        BigDecimal percentDecimal = percent.divide(hundred);
        BigDecimal amountToSubtract = sum.multiply(percentDecimal);
        return sum.subtract(amountToSubtract);
    }
}
