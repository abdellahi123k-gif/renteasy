package com.renteasy.renteasy.repository;

import com.renteasy.renteasy.entity.Reservation;
import com.renteasy.renteasy.entity.ReservationStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @EntityGraph(attributePaths = {"locataire", "logement"})
    List<Reservation> findAll();

    @EntityGraph(attributePaths = {"locataire", "logement"})
    Optional<Reservation> findById(Long id);

    long countByLocataire_Id(Long locataireId);

    long countByLocataire_IdAndStatus(Long locataireId, ReservationStatus status);

    long countByLogement_Owner_Id(Long ownerId);

    @EntityGraph(attributePaths = {"locataire", "logement"})
    List<Reservation> findByLocataire_Id(Long locataireId);

    @Query("SELECT r FROM Reservation r JOIN FETCH r.locataire JOIN FETCH r.logement l WHERE l.owner.id = :ownerId")
    List<Reservation> findByLogementOwnerId(@Param("ownerId") Long ownerId);

    @Query("""
            SELECT COUNT(r) > 0 FROM Reservation r
            WHERE r.logement.id = :logementId
            AND r.status <> 'ANNULEE'
            AND r.dateDebut < :dateFin
            AND r.dateFin > :dateDebut
            """)
    boolean existsByOverlappingDates(
            @Param("logementId") Long logementId,
            @Param("dateDebut") LocalDate dateDebut,
            @Param("dateFin") LocalDate dateFin
    );
}
