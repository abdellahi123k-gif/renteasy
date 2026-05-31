package com.renteasy.renteasy.repository;

import com.renteasy.renteasy.entity.Annonce;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AnnonceRepository
        extends JpaRepository<Annonce, Long> {

    @EntityGraph(attributePaths = {"logement", "logement.owner"})
    List<Annonce> findAll();

    @EntityGraph(attributePaths = {"logement", "logement.owner"})
    List<Annonce> findByActiveTrue();

    @EntityGraph(attributePaths = {"logement", "logement.owner"})
    Optional<Annonce> findById(Long id);

    @Query("SELECT COUNT(a) FROM Annonce a WHERE a.logement.owner.id = :ownerId")
    long countByLogementOwnerId(@Param("ownerId") Long ownerId);

    @EntityGraph(attributePaths = {"logement", "logement.owner"})
    @Query("SELECT a FROM Annonce a WHERE a.logement.owner.id = :ownerId")
    List<Annonce> findByLogementOwnerId(@Param("ownerId") Long ownerId);
}
