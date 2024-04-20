package com.example.bankapplicatopm.dto.account;

import com.example.bankapplicatopm.dto.wallet.WalletDTO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AdminAccountDTO {
    private String userName;
    private String iban;
    private List <WalletDTO> walletDTO = new ArrayList<>();
}
