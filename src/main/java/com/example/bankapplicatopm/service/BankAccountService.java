package com.example.bankapplicatopm.service;

import com.example.bankapplicatopm.dto.transactions.TransactionDTO;
import com.example.bankapplicatopm.emailSender.EmailSender;
import com.example.bankapplicatopm.enums.UserRole;
import com.example.bankapplicatopm.model.BankAccount;
import com.example.bankapplicatopm.model.Wallet;
import com.example.bankapplicatopm.model.token.ChangePasswordRequestToken;
import com.example.bankapplicatopm.model.token.EmailConfirmationToken;
import com.example.bankapplicatopm.repository.BankAccountRepository;
import com.example.bankapplicatopm.util.CurrentUser;
import com.example.bankapplicatopm.util.EmailBuilder;
import com.example.bankapplicatopm.util.IBANGenerated;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service

public class BankAccountService implements UserDetailsService {
    private final EmailSender emailSender;
    private final BankAccountRepository bankAccountRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EmailConfirmationTokenService emailConfirmationTokenService;
    private final ChangePasswordRequestTokenService changePasswordRequestTokenService;
    private final WalletService walletService;

    public BankAccountService(EmailSender emailSender,
                              BankAccountRepository bankAccountRepository,
                              BCryptPasswordEncoder bCryptPasswordEncoder,
                              EmailConfirmationTokenService emailConfirmationTokenService,
                              ChangePasswordRequestTokenService changePasswordRequestTokenService,
                              @Lazy WalletService walletService) {
        this.emailSender = emailSender;
        this.bankAccountRepository = bankAccountRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.emailConfirmationTokenService = emailConfirmationTokenService;
        this.changePasswordRequestTokenService = changePasswordRequestTokenService;
        this.walletService = walletService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return bankAccountRepository.findUserAccountByEmail(email).orElseThrow
                (() -> new UsernameNotFoundException("User with this email not found: " + email));
    }

    @Transactional
    public String singUpUser(BankAccount bankAccount) {
        boolean userExists = bankAccountRepository.findUserAccountByEmail(bankAccount.getEmail()).isPresent();
        if (userExists) {
            checkValidUser(bankAccount);
        }

        String encodePassword = bCryptPasswordEncoder.encode(bankAccount.getPassword());
        bankAccount.setPassword(encodePassword);

        bankAccountRepository.save(bankAccount);
        walletService.createBaseWallet(bankAccount, "UAH");

        bankAccount.setSpecialName("@user" + bankAccount.getId());
        bankAccount.setIBAN(IBANGenerated.generator(String.valueOf(bankAccount.getId())));
        bankAccountRepository.save(bankAccount);

        return creatConfirmToken(bankAccount);
    }

    @Transactional
    public BankAccount sendMoneyFromAdminPanel(TransactionDTO request) {
        BankAccount bankAccount = findBankAccountByIBAN(request.getToAccount());
        if (bankAccount != null) {
            for (Wallet w : bankAccount.getWallets()) {
                if (w.getCurrency().equals(request.getCurrency())) {
                    w.setSum(w.getSum().add(request.getSum()));
                }
            }
            return bankAccountRepository.save(bankAccount);
        } else throw new IllegalStateException("User was not Found");
    }

    @Transactional
    public boolean sendMoney(BigDecimal sum, String toAccountEmail, String currency) {
        BankAccount fromAccount = CurrentUser.getCurrentUser();
        BankAccount toAccount = bankAccountRepository.findBankAccountByIBAN(toAccountEmail);

        Wallet fromWallet = walletService.findWalletByAccountIdAndCurrency(fromAccount.getId(), currency);

        if (fromWallet.getSum().compareTo(sum) < 0) {
            throw new IllegalStateException("Not enough money on balance");
        }

        Wallet toWallet = walletService.findWalletByAccountIdAndCurrency(toAccount.getId(), currency);
        if (toWallet == null) {
            toWallet = walletService.createWalletForSendBankAccount(toAccount.getId(), currency);
        }
        fromWallet.setSum(fromWallet.getSum().subtract(sum));
        toWallet.setSum(toWallet.getSum().add(sum));

        walletService.save(fromWallet);
        walletService.save(toWallet);

        return true;
    }


    @Transactional
    public BankAccount forgotPassword(String email) {
        String link = "https://kosto-app-bank-53a05f6291cb.herokuapp.com/change-password?token=";
        BankAccount bankAccount = bankAccountRepository.findByEmail(email);
        if (bankAccount != null) {
            String token = creatChangeForgotPasswordToken(bankAccount);
            emailSender.send(bankAccount.getEmail(), EmailBuilder.requestForChanePassword(bankAccount.getFirstName(), link + token));
        } else
            throw new IllegalStateException("User not found with this email: " + email);

        return bankAccount;
    }

    @Transactional
    public void changeForgetPasswordByToken(String token, String newPassword) {
        ChangePasswordRequestToken changePasswordRequestToken =
                changePasswordRequestTokenService.findChangePasswordRequestTokenByToken(token);
        BankAccount bankAccount = changePasswordRequestToken.getBankAccount();
        if (bankAccount != null) {
            String encodePassword = bCryptPasswordEncoder.encode(newPassword);
            bankAccount.setPassword(encodePassword);
            bankAccountRepository.save(bankAccount);
            changePasswordRequestTokenService.deleteChangePasswordRequestTokenByToken(token);
        } else
            throw new IllegalStateException("Bank account was not find");
    }


    @Transactional
    public void createTestUser() {
        for (int i = 0; i < 10; i++) {
            BankAccount bankAccount = new BankAccount();
            String encodePassword = bCryptPasswordEncoder.encode("pass");
            bankAccount.setPassword(encodePassword);
            bankAccount.setEmail("admin" + i + "@gmail.com");
            bankAccount.setUserRole(UserRole.ROLE_ADMIN);
            bankAccountRepository.save(bankAccount);
            walletService.createBaseWallet(bankAccount, "UAH");
            bankAccount.setEnabled(true);
            bankAccount.setSpecialName("@user" + bankAccount.getId());
            bankAccount.setIBAN(IBANGenerated.generator(String.valueOf(bankAccount.getId())));
            bankAccountRepository.save(bankAccount);
        }

    }

    @Transactional
    public void currentAccountToAdmin() {
        BankAccount bankAccount = CurrentUser.getCurrentUser();
        bankAccount.setUserRole(UserRole.ROLE_ADMIN);
        bankAccountRepository.save(bankAccount);
    }

    @Transactional
    public void currentAccountToUser() {
        BankAccount bankAccount = CurrentUser.getCurrentUser();
        bankAccount.setUserRole(UserRole.ROLE_USER);
        bankAccountRepository.save(bankAccount);
    }

    @Transactional
    public void save(BankAccount account) {
        bankAccountRepository.save(account);
    }

    @Transactional
    public BankAccount findBankAccountByIBAN(String iban) {
        return bankAccountRepository.findBankAccountByIBAN(iban);
    }

    @Transactional
    public BankAccount findBankAccountById(Long id) {
        return bankAccountRepository.findBankAccountById(id);
    }

    @Transactional
    public List<BankAccount> getAllUsers() {
        return bankAccountRepository.findAll();
    }

    @Transactional
    public Optional<BankAccount> findUserAccountByEmail(String email) {
        return bankAccountRepository.findUserAccountByEmail(email);
    }

    @Transactional
    public BankAccount findByEmail(String email) {
        return bankAccountRepository.findByEmail(email);
    }

    @Transactional
    public void enableAppUser(String email) {
        bankAccountRepository.enableAppUser(email);
    }

    @Transactional
    public Page<BankAccount> getAllUsers(Pageable pageable) {
        return bankAccountRepository.findAll(pageable);
    }

    private String creatChangeForgotPasswordToken(BankAccount bankAccount) {
        String token = UUID.randomUUID().toString();
        ChangePasswordRequestToken changePasswordRequestToken = new ChangePasswordRequestToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(5),
                bankAccount
        );
        changePasswordRequestTokenService.save(changePasswordRequestToken);
        return token;
    }

    private void checkValidUser(BankAccount bankAccount){
        BankAccount account = bankAccountRepository.getUserAccountByEmail(bankAccount.getEmail());
        if (!account.isEnabled()) {
            EmailConfirmationToken emailConfirmationToken = emailConfirmationTokenService.findConfirmationTokenByUserAccount(account);
            String link = "https://kosto-app-bank-53a05f6291cb.herokuapp.com/registration/confirm?token=";
            if (emailConfirmationToken == null) {
                String token = creatConfirmToken(account);
                emailSender.send(account.getEmail(), EmailBuilder.confirmYourEmail(account.getFirstName(), link + token));
                throw new IllegalStateException("User was not expired. We send new code on your Email address");
            } else if (emailConfirmationToken.getConfirmedAt() == null) {
                emailSender.send(account.getEmail(), EmailBuilder.confirmYourEmail(account.getFirstName(), link + emailConfirmationToken.getToken()));
                throw new IllegalStateException("User was not expired. We re-send your code on your Email address");
            }
        }
        throw new IllegalStateException("User already exist");
    }

    private String creatConfirmToken(BankAccount bankAccount) {
        String token = UUID.randomUUID().toString();
        EmailConfirmationToken emailConfirmationToken = new EmailConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(60),
                bankAccount
        );
        emailConfirmationTokenService.saveConfirmationToken(emailConfirmationToken);
        return token;
    }


}
