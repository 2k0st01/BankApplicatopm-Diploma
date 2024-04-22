package com.example.bankapplicatopm.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@org.springframework.stereotype.Controller
public class MainController {

    @GetMapping("/account")
    public String getAccountPage(){
        return "account/index";
    }

    @GetMapping("/login")
    public String getLoginPage(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/account";
        }
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
}
