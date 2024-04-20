package com.example.bankapplicatopm.repository;


import com.example.bankapplicatopm.model.Jar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JarRepository extends JpaRepository<Jar, Long> {

    Jar findJarById(Long id);
}
