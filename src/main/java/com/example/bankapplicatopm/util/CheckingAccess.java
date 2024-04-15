package com.example.bankapplicatopm.util;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CheckingAccess {

    public static boolean isAdmin(User user) {
        Collection<GrantedAuthority> roles = user.getAuthorities();
        for (GrantedAuthority auth : roles) {
            if ("ROLE_ADMIN".equals(auth.getAuthority()))
                return true;
        }
        return false;
    }
}
