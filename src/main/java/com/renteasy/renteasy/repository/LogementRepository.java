package com.renteasy.renteasy.repository;



import com.renteasy.renteasy.entity.Logement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface LogementRepository extends JpaRepository<Logement, Long> {
    List<Logement> findByVille(String ville);

    List<Logement> findByType(String type);

    List<Logement> findByDisponible(boolean disponible);

    List<Logement> findByPrixLessThanEqual(BigDecimal prix);
}