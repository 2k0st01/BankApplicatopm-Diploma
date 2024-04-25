package com.example.bankapplicatopm.repository;

import com.example.bankapplicatopm.model.CurrencyRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CurrencyRepository extends JpaRepository<CurrencyRate, Long> {
    CurrencyRate findCurrencyModelByCc(String cc);

    @Query("SELECT c FROM CurrencyRate c")
    List<CurrencyRate> getAll();
}
