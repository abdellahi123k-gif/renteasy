package com.renteasy.renteasy.service.impl;

import com.renteasy.renteasy.dto.request.ReservationRequestDTO;
import com.renteasy.renteasy.dto.response.ReservationResponseDTO;
import com.renteasy.renteasy.entity.*;
import com.renteasy.renteasy.exception.ResourceNotFoundException;
import com.renteasy.renteasy.mapper.ReservationMapper;
import com.renteasy.renteasy.repository.LogementRepository;
import com.renteasy.renteasy.repository.ReservationRepository;
import com.renteasy.renteasy.repository.UserRepository;
import com.renteasy.renteasy.service.ReservationService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl
        implements ReservationService {

    private final ReservationRepository reservationRepository;

    private final UserRepository userRepository;

    private final LogementRepository logementRepository;

    @Override
    public ReservationResponseDTO createReservation(
            ReservationRequestDTO dto
    ) {

        // Validation des dates
        if (dto.getDateDebut().isAfter(dto.getDateFin())) {

            throw new RuntimeException(
                    "Start date cannot be after end date"
            );
        }

        // Vérifier locataire
        User locataire = userRepository.findById(
                        dto.getLocataireId()
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Locataire not found"
                        )
                );

        // Vérifier logement
        Logement logement = logementRepository.findById(
                        dto.getLogementId()
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Logement not found"
                        )
                );

        // Vérifier disponibilité
        if (!logement.isDisponible()) {

            throw new RuntimeException(
                    "Logement is not available"
            );
        }

        // Création réservation
        Reservation reservation = Reservation.builder()
                .dateDebut(dto.getDateDebut())
                .dateFin(dto.getDateFin())
                .status(ReservationStatus.EN_ATTENTE)
                .locataire(locataire)
                .logement(logement)
                .build();

        Reservation saved =
                reservationRepository.save(reservation);

        return ReservationMapper.toResponse(saved);
    }

    @Override
    public List<ReservationResponseDTO> getAllReservations() {

        return reservationRepository.findAll()
                .stream()
                .map(ReservationMapper::toResponse)
                .toList();
    }
}
