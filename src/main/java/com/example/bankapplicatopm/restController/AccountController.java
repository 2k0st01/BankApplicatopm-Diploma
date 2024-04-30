package com.example.bankapplicatopm.restController;

import com.example.bankapplicatopm.dto.account.AccountDTO;
import com.example.bankapplicatopm.model.BankAccount;
import com.example.bankapplicatopm.service.BankAccountService;
import com.example.bankapplicatopm.util.CurrentUser;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {
    private final BankAccountService bankAccountService;

    @GetMapping("/data")
    public AccountDTO getAccountData(){
        BankAccount bankAccount = CurrentUser.getCurrentUser();
        AccountDTO accountDTO = new AccountDTO();
        if(bankAccount != null){
            accountDTO.setEmail(bankAccount.getEmail());
            accountDTO.setFirstName(bankAccount.getFirstName());
            accountDTO.setIBAN(bankAccount.getIBAN());
            accountDTO.setAddress(bankAccount.getAddress());
            accountDTO.setSpecialName(bankAccount.getSpecialName());
            accountDTO.setDateOfBirth(bankAccount.getDateOfBirth());
            accountDTO.setLastName(bankAccount.getLastName());
            accountDTO.setPhone(bankAccount.getPhone());
            accountDTO.setUserRole(bankAccount.getUserRole());
        }
        return accountDTO;
    }

    @GetMapping("/test")
    public String test(){
        return "Okey";
    }


//    @PostMapping()
//    public void changePassword(){
//
//    }
//
//    @PostMapping()
//    public void changeAddress(){
//
//    }
//
//    @PostMapping()
//    public void changeSpecialName(){
//
//    }
//
//    @PostMapping()
//    public void changePhoneNumber(){
//
//    }

}
