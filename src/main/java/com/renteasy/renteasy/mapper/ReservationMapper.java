package com.renteasy.renteasy.mapper;

import com.renteasy.renteasy.dto.response.ReservationResponseDTO;
import com.renteasy.renteasy.entity.Reservation;

public class ReservationMapper {

    public static ReservationResponseDTO toResponse(
            Reservation reservation
    ) {

        return ReservationResponseDTO.builder()
                .id(reservation.getId())
                .dateDebut(reservation.getDateDebut())
                .dateFin(reservation.getDateFin())
                .status(reservation.getStatus().name())
                .locataireId(reservation.getLocataire().getId())
                .locataireName(
                        reservation.getLocataire().getFirstName()
                )
                .logementId(reservation.getLogement().getId())
                .logementTitre(
                        reservation.getLogement().getTitre()
                )
                .build();
    }
}