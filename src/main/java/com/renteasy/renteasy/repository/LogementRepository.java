package com.renteasy.renteasy.repository;

import com.renteasy.renteasy.entity.Logement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;

public interface LogementRepository extends JpaRepository<Logement, Long> {

    @EntityGraph(attributePaths = {"owner"})
    Page<Logement> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"owner"})
    Optional<Logement> findById(Long id);

    @EntityGraph(attributePaths = {"owner"})
    Page<Logement> findByOwner_Id(Long ownerId, Pageable pageable);

    @EntityGraph(attributePaths = {"owner"})
    Page<Logement> findByOwner_IdAndDisponibleTrue(Long ownerId, Pageable pageable);

    long countByOwner_Id(Long ownerId);

    @Query("""
            SELECT l FROM Logement l
            WHERE (:ville IS NULL OR l.ville = :ville)
            AND (:type IS NULL OR l.type = :type)
            AND (:minPrix IS NULL OR l.prix >= :minPrix)
            AND (:maxPrix IS NULL OR l.prix <= :maxPrix)
            AND (:disponible IS NULL OR l.disponible = :disponible)
            """)
    @EntityGraph(attributePaths = {"owner"})
    Page<Logement> search(
            @Param("ville") String ville,
            @Param("type") String type,
            @Param("minPrix") BigDecimal minPrix,
            @Param("maxPrix") BigDecimal maxPrix,
            @Param("disponible") Boolean disponible,
            Pageable pageable
    );
}
