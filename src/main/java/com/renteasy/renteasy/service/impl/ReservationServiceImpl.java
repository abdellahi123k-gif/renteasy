package com.renteasy.renteasy.service.impl;

import com.renteasy.renteasy.dto.request.ReservationRequestDTO;
import com.renteasy.renteasy.dto.response.ReservationResponseDTO;
import com.renteasy.renteasy.entity.*;
import com.renteasy.renteasy.exception.LogementNotAvailableException;
import com.renteasy.renteasy.exception.ReservationConflictException;
import com.renteasy.renteasy.exception.ResourceNotFoundException;
import com.renteasy.renteasy.exception.UnauthorizedActionException;
import com.renteasy.renteasy.mapper.ReservationMapper;
import com.renteasy.renteasy.repository.LogementRepository;
import com.renteasy.renteasy.repository.ReservationRepository;
import com.renteasy.renteasy.security.SecurityUtils;
import com.renteasy.renteasy.service.ReservationService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl
        implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final LogementRepository logementRepository;
    private final SecurityUtils securityUtils;

    @Override
    @Transactional
    public ReservationResponseDTO createReservation(
            ReservationRequestDTO dto
    ) {
        if (dto.getDateDebut().isAfter(dto.getDateFin())) {
            throw new IllegalArgumentException(
                    "Start date cannot be after end date"
            );
        }
        User locataire = securityUtils.getCurrentUser();
        Logement logement = logementRepository.findById(
                        dto.getLogementId()
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Logement not found"
                        )
                );
        if (!logement.isDisponible()) {
            throw new LogementNotAvailableException(
                    "Logement is not available"
            );
        }
        if (reservationRepository.existsByOverlappingDates(
                dto.getLogementId(), dto.getDateDebut(), dto.getDateFin()
        )) {
            throw new ReservationConflictException(
                    "Logement already reserved for the requested dates"
            );
        }
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
    @Transactional(readOnly = true)
    public List<ReservationResponseDTO> getAllReservations() {
        User currentUser = securityUtils.getCurrentUser();
        String role = currentUser.getRole().getName();
        if ("ADMIN".equals(role)) {
            return reservationRepository.findAll()
                    .stream()
                    .map(ReservationMapper::toResponse)
                    .toList();
        } else if ("PROPRIETAIRE".equals(role)) {
            return reservationRepository.findByLogementOwnerId(currentUser.getId())
                    .stream()
                    .map(ReservationMapper::toResponse)
                    .toList();
        } else {
            return reservationRepository.findByLocataire_Id(currentUser.getId())
                    .stream()
                    .map(ReservationMapper::toResponse)
                    .toList();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public ReservationResponseDTO getReservationById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Reservation not found")
                );
        User currentUser = securityUtils.getCurrentUser();
        String role = currentUser.getRole().getName();
        boolean isLocataire = reservation.getLocataire().getId().equals(currentUser.getId());
        boolean isOwner = reservation.getLogement().getOwner().getId().equals(currentUser.getId());
        if (!"ADMIN".equals(role) && !isLocataire && !isOwner) {
            throw new UnauthorizedActionException(
                    "You can only view your own reservations"
            );
        }
        return ReservationMapper.toResponse(reservation);
    }

    @Override
    @Transactional
    public ReservationResponseDTO confirmReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Reservation not found")
                );
        User currentUser = securityUtils.getCurrentUser();
        if (!reservation.getLogement().getOwner().getId().equals(currentUser.getId())) {
            throw new UnauthorizedActionException(
                    "Only the logement owner can confirm reservations"
            );
        }
        if (reservation.getStatus() != ReservationStatus.EN_ATTENTE) {
            throw new IllegalArgumentException(
                    "Only pending reservations can be confirmed"
            );
        }
        reservation.setStatus(ReservationStatus.CONFIRMEE);
        return ReservationMapper.toResponse(reservation);
    }

    @Override
    @Transactional
    public ReservationResponseDTO cancelReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Reservation not found")
                );
        User currentUser = securityUtils.getCurrentUser();
        boolean isLocataire = reservation.getLocataire().getId().equals(currentUser.getId());
        boolean isOwner = reservation.getLogement().getOwner().getId().equals(currentUser.getId());
        if (!isLocataire && !isOwner) {
            throw new UnauthorizedActionException(
                    "Only the reservation owner or logement owner can cancel"
            );
        }
        if (reservation.getStatus() == ReservationStatus.ANNULEE) {
            throw new IllegalArgumentException("Reservation is already cancelled");
        }
        reservation.setStatus(ReservationStatus.ANNULEE);
        return ReservationMapper.toResponse(reservation);
    }

    @Override
    @Transactional
    public void deleteReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Reservation not found")
                );
        User currentUser = securityUtils.getCurrentUser();
        boolean isLocataire = reservation.getLocataire().getId().equals(currentUser.getId());
        boolean isOwner = reservation.getLogement().getOwner().getId().equals(currentUser.getId());
        if (!isLocataire && !isOwner) {
            throw new UnauthorizedActionException(
                    "You can only delete your own reservations"
            );
        }
        reservationRepository.deleteById(id);
    }
}
