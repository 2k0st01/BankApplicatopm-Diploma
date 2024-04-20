package com.example.bankapplicatopm.restController;

import com.example.bankapplicatopm.dto.account.AdminAccountDTO;
import com.example.bankapplicatopm.dto.transactions.TransactionDTO;
import com.example.bankapplicatopm.dto.wallet.WalletDTO;
import com.example.bankapplicatopm.model.BankAccount;
import com.example.bankapplicatopm.model.Wallet;
import com.example.bankapplicatopm.service.BankAccountService;
import com.example.bankapplicatopm.service.TransactionService;
import com.example.bankapplicatopm.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AdminPanelController {

    private final BankAccountService bankAccountService;
    private final TransactionService transactionService;

    private static final int PAGE_SIZE = 5;

    @GetMapping("/panel/getUsers")
    public Page<AdminAccountDTO> getUsers(@RequestParam(required = false, defaultValue = "0") int page) {
        Page<BankAccount> bankAccounts = bankAccountService.getAllUsers(PageRequest.of(page, PAGE_SIZE, Sort.Direction.DESC, "id"));
        return bankAccounts.map(this::convertToDTO);
    }

    @PostMapping("/transaction/sendFromAdmin")
    public ResponseEntity<ApiResponse> sendMoney(@RequestBody TransactionDTO request){
        try {
            BankAccount bankAccount = bankAccountService.sendMoneyFromAdminPanel(request);
            return ResponseEntity.ok(new ApiResponse(true, "Transaction was created", bankAccount));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false,
                    "Some error: " + e.getMessage()));
        }

    }

    private AdminAccountDTO convertToDTO(BankAccount account) {
        AdminAccountDTO accountDTO = new AdminAccountDTO();
        accountDTO.setIban(account.getIBAN());
        accountDTO.setUserName(account.getEmail());

        for(Wallet wallet : account.getWallets()){
            WalletDTO walletDTO = new WalletDTO();
            walletDTO.setCurrency(wallet.getCurrency());
            walletDTO.setSum(wallet.getSum());
            accountDTO.getWalletDTO().add(walletDTO);
        }
        return accountDTO;
    }
}
