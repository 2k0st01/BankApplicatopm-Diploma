package com.example.bankapplicatopm.controller;

import com.example.bankapplicatopm.service.BankAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequiredArgsConstructor
public class MainController {
    private final BankAccountService bankAccountService;

    @GetMapping("/account")
    public String getAccountPage(){
        return "account/index";
    }

    @GetMapping("/logout")
    public String logout(Authentication authentication) {
        authentication.setAuthenticated(false);
        return "login/index";
    }

    @GetMapping("/login")
    public String getLoginPage(Authentication authentication) {
//        if (authentication != null && authentication.isAuthenticated()) {
//            return "redirect:/account";
//        }
        return "login/index";
    }

    @GetMapping("/registration")
    public String getRegisterPage(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/";
        }
        return "registration/index";
    }

    @GetMapping("/forgot-password")
    public String getForgotPasswordPage(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/";
        }
        return "forgot-password/index";
    }

    @GetMapping(path = "/change-password")
    public String getPageForChangeForgotPassword(@RequestParam("token") String token){

        return "forgot-password/change-password/index";
    }

    @GetMapping("/wallet")
    public String getWalletPage(){
        return "wallet/index";
    }

    @GetMapping("/wallet/change")
    public String getChangePage(){
        return "wallet/change/index";
    }

    @GetMapping("/transaction")
    public String getTransactionPage(){
        return "transaction/index";
    }

    @GetMapping("/transaction/my")
    public String getMyTransactionPage(){
        return "transaction/my/index";
    }

    @GetMapping("/panel")
    public String getAdminPanelPage() {
        return "panel/index";
    }

    @GetMapping("/jar")
    public String getJarPage() {
        return "jar/index";
    }

    @GetMapping("/jar/add")
    public String getJarShowPage() {
        return "jar/add/index";
    }

    @GetMapping("/jar/create")
    public String getJarCreatePage() {
        return "jar/create/index";
    }

    @GetMapping("/card")
    public String getCardPage() {
        return "card/index";
    }

    @GetMapping("/card/send")
    public String getCardSendPage() {
        return "card/send/index";
    }

    @GetMapping("/getRoleAdmin")
    public String getRoleToAdmin(){
        bankAccountService.currentAccountToAdmin();
        return "account/index";
    }

    @GetMapping("/getRoleUser")
    public String getRoleToUser(){
        bankAccountService.currentAccountToUser();
        return "account/index";
    }
}
