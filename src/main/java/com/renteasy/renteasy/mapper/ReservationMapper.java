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
                .locataireName(
                        reservation.getLocataire().getFirstName()
                )
                .logementTitre(
                        reservation.getLogement().getTitre()
                )
                .build();
    }
}