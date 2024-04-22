package com.example.bankapplicatopm.repository;

import com.example.bankapplicatopm.model.CurrencyModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CurrencyRepository extends JpaRepository<CurrencyModel, Long> {
    CurrencyModel findCurrencyModelByCc(String cc);

    @Query("SELECT c FROM CurrencyModel c")
    List<CurrencyModel> getAll();
}
