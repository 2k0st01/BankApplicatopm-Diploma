package com.example.bankapplicatopm.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

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

}
