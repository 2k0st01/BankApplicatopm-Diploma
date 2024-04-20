package com.example.bankapplicatopm.restController;


import com.example.bankapplicatopm.dto.wallet.RegistrationWalletDTO;
import com.example.bankapplicatopm.dto.wallet.WalletDTO;
import com.example.bankapplicatopm.model.Wallet;
import com.example.bankapplicatopm.service.WalletService;
import com.example.bankapplicatopm.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @GetMapping("/wallet/get")
    public List<WalletDTO> getAllBankAccountWalletsCurrencyAndSum() {
        List<WalletDTO> list = walletService.findWalletByAccountId();
        return list;
    }

    @PostMapping("/wallet")
    public ResponseEntity<ApiResponse> walletPage(RegistrationWalletDTO request) {
        try {
            Wallet wallet = walletService.createWallet(request.getCurrency());
            return ResponseEntity.ok(new ApiResponse(true, "Wallet was created", wallet));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false,
                    "Some error: " + e.getMessage()));
        }
    }
}
