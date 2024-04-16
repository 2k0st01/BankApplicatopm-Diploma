package com.example.bankapplicatopm.repository;

import com.example.bankapplicatopm.model.token.ChangePasswordRequestToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChangePasswordRequestTokenRepository  extends JpaRepository<ChangePasswordRequestToken, Long> {

    ChangePasswordRequestToken findChangePasswordRequestTokenByToken(String token);

    void deleteChangePasswordRequestTokenByToken(String token);

    List <ChangePasswordRequestToken> findByConfirmedAtIsNull();
}
