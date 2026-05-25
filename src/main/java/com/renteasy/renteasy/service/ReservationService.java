package com.renteasy.renteasy.service;

import com.renteasy.renteasy.dto.request.ReservationRequestDTO;
import com.renteasy.renteasy.dto.response.ReservationResponseDTO;

import java.util.List;

public interface ReservationService {

    ReservationResponseDTO createReservation(
            ReservationRequestDTO dto
    );

    List<ReservationResponseDTO> getAllReservations();
}