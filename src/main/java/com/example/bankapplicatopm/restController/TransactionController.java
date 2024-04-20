package com.example.bankapplicatopm.restController;

import com.example.bankapplicatopm.dto.transactions.AccountTransactionDTO;
import com.example.bankapplicatopm.dto.transactions.TransactionDTO;
import com.example.bankapplicatopm.model.BankAccount;
import com.example.bankapplicatopm.model.Transaction;
import com.example.bankapplicatopm.service.TransactionService;
import com.example.bankapplicatopm.util.ApiResponse;
import com.example.bankapplicatopm.util.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    private static final int PAGE_SIZE = 5;

    @PostMapping("/transaction/send")
    public ResponseEntity<ApiResponse> sendMoney(@RequestBody TransactionDTO request){
        try {
            Transaction transaction = transactionService.addTransaction
                    (request.getToAccount(),request.getSum(),request.getComment(),request.getCurrency());
            return ResponseEntity.ok(new ApiResponse(true, "Transaction was created", transaction));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false,
                    "Some error: " + e.getMessage()));
        }
    }

    @GetMapping("/transaction/getMy")
    public List<AccountTransactionDTO> getMyTransactions(){
        BankAccount bankAccount = CurrentUser.getCurrentUser();
        List<Transaction> list = transactionService.getUserTransactions(bankAccount.getId());
        List<AccountTransactionDTO> main = list.stream()
                .map(t -> {
                    AccountTransactionDTO dto = new AccountTransactionDTO();
                    dto.setDate(t.getDate());
                    dto.setSum(t.getSum());
                    dto.setComment(t.getComment());
                    dto.setCurrency(t.getCurrency());
                    dto.setToAccount(t.getToAccount());
                    dto.setFromAccount(t.getFromAccount());
                    return dto;
                })
                .collect(Collectors.toList());
        return main;
    }
}
