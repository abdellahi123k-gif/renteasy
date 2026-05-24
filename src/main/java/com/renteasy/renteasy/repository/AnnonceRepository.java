package com.renteasy.renteasy.repository;



import com.renteasy.renteasy.entity.Annonce;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnonceRepository extends JpaRepository<Annonce, Long> {
}
