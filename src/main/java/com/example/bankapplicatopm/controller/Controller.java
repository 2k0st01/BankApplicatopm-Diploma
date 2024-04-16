package com.example.bankapplicatopm.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@org.springframework.stereotype.Controller
public class Controller {

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

    @GetMapping("/register")
    public String getRegisterPage(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/";
        }
        return "register/index";
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
}
