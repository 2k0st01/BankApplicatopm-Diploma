package com.example.bankapplicatopm.service;

import com.example.bankapplicatopm.model.BankAccount;
import com.example.bankapplicatopm.model.Card;
import com.example.bankapplicatopm.model.Wallet;
import com.example.bankapplicatopm.repository.CardRepository;
import com.example.bankapplicatopm.util.CurrentUser;
import com.example.bankapplicatopm.util.MastercardGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;
    private final WalletService walletService;
    private final TransactionService transactionService;

    @Transactional
    public void save(Card card) {
        cardRepository.save(card);
    }

    @Transactional
    public Card findCardByNumberCard(String number) {
        return cardRepository.findCardByNumberCard(number);
    }

    @Transactional
    public List<Card> findCardByBankAccount() {
        BankAccount bankAccount = CurrentUser.getCurrentUser();
        return cardRepository.findCardByBankAccount(bankAccount);
    }

    @Transactional
    public Card createCard(String currency) {
        BankAccount bankAccount = CurrentUser.getCurrentUser();
        Card card = new Card();
        card.setNumberCard(MastercardGenerator.generateMastercardNumber());
        if (checkValid(card.getNumberCard())) {
            createCard(currency);
        }
        card.setDate(MastercardGenerator.getDate());
        card.setCvv(MastercardGenerator.getCVV());
        card.setUserName(bankAccount.getFirstName() + " " + bankAccount.getLastName());
        card.setBankAccount(bankAccount);
        Wallet wallet = walletService.findWalletByAccountIdAndCurrency(bankAccount.getId(), currency);


        if (wallet == null) {
            wallet = walletService.createWallet(currency);
        }
        card.setWallet(wallet);
        return cardRepository.save(card);
    }

    @Transactional
    public List<Card> findCardByDate(String date) {
        return cardRepository.findCardByDate(date);
    }

    public boolean sendMoneyByCards(String fromCard, String toCard, BigDecimal sum, String comment) {
        Card findFromCard = cardRepository.findCardByNumberCard(fromCard);
        Card findToCard = cardRepository.findCardByNumberCard(toCard);

        if (findToCard == null || findFromCard == null)
            throw new IllegalStateException("Card number not found");

        if(findFromCard.getBankAccount() == findToCard.getBankAccount())
            throw new IllegalStateException("You try make transaction on same account");

        if (!findFromCard.isEnable() && !findToCard.isEnable())
            throw new IllegalStateException("Some card already not enable");

        if (findFromCard.getWallet().getSum().compareTo(sum) < 0)
            throw new IllegalStateException("Not enough money on balance");

        if (!findToCard.getWallet().getCurrency().equals(findFromCard.getWallet().getCurrency()))
            throw new IllegalStateException("Cards have not same currency");


        findFromCard.getWallet().setSum(findFromCard.getWallet().getSum().subtract(sum));
        findToCard.getWallet().setSum(findToCard.getWallet().getSum().add(sum));

        transactionService.addCardTransaction(findFromCard,findToCard,sum,comment);
        cardRepository.save(findToCard);
        cardRepository.save(findFromCard);

        return true;
    }

    public boolean checkValid(String number) {
        Card card = cardRepository.findCardByNumberCard(number);
        return card != null;
    }
}
