package com.renteasy.renteasy.repository;



import com.renteasy.renteasy.entity.Logement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogementRepository extends JpaRepository<Logement, Long> {
}