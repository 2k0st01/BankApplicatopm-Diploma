package com.example.bankapplicatopm.service;

import com.example.bankapplicatopm.model.BankAccount;
import com.example.bankapplicatopm.model.CurrencyModel;
import com.example.bankapplicatopm.model.Wallet;
import com.example.bankapplicatopm.repository.CurrencyRepository;
import com.example.bankapplicatopm.util.CurrentUser;
import lombok.NonNull;
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

        doTransaction(walletFrom,walletTo,findFrom,findTo,sum);
    }

    private void doTransaction(Wallet from, Wallet to, CurrencyModel findFrom, CurrencyModel findTo, BigDecimal sum) {
        if (findFrom == null && from.getCurrency().equals("UAH") && !to.getCurrency().equals("UAH")) {
            from.setSum(from.getSum().subtract(sum));
            sum = sum.divide(findTo.getRate(), 2, RoundingMode.HALF_EVEN);
            to.setSum(to.getSum().add(sum));
            walletService.save(from);
            walletService.save(to);
        } else if (findTo == null && to.getCurrency().equals("UAH") && !from.getCurrency().equals("UAH")) {
            from.setSum(from.getSum().subtract(sum));
            to.setSum(to.getSum().add(findFrom.getRate().multiply(sum)));
            walletService.save(from);
            walletService.save(to);
        } else {
            BigDecimal sumToUAH = new BigDecimal("0").add(findFrom.getRate().multiply(sum));
            from.setSum(from.getSum().subtract(sum));
            sumToUAH = sumToUAH.divide(findTo.getRate(), 2, RoundingMode.HALF_EVEN);
            to.setSum(to.getSum().add(sumToUAH));
            walletService.save(from);
            walletService.save(to);
        }
    }

}
