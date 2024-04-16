package com.example.bankapplicatopm.service;

import com.example.bankapplicatopm.model.token.ChangePasswordRequestToken;
import com.example.bankapplicatopm.repository.ChangePasswordRequestTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ChangePasswordRequestTokenService {
    private final ChangePasswordRequestTokenRepository changePasswordRequestTokenRepository;

    public void save(ChangePasswordRequestToken changePasswordRequestToken){
        changePasswordRequestTokenRepository.save(changePasswordRequestToken);
    }

    public ChangePasswordRequestToken findChangePasswordRequestTokenByToken(String token){
        return changePasswordRequestTokenRepository.findChangePasswordRequestTokenByToken(token);
    }

    public void deleteChangePasswordRequestTokenByToken(String token){
        changePasswordRequestTokenRepository.deleteChangePasswordRequestTokenByToken(token);
    }

    public List<ChangePasswordRequestToken> findByConfirmedAtIsNull(){
        return changePasswordRequestTokenRepository.findByConfirmedAtIsNull();
    }
}
