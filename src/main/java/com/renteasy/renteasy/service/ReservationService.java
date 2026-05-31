package com.renteasy.renteasy.service;

import com.renteasy.renteasy.dto.request.ReservationRequestDTO;
import com.renteasy.renteasy.dto.response.ReservationResponseDTO;

import java.util.List;

public interface ReservationService {

    ReservationResponseDTO createReservation(ReservationRequestDTO dto);

    List<ReservationResponseDTO> getAllReservations();

    ReservationResponseDTO getReservationById(Long id);

    ReservationResponseDTO confirmReservation(Long id);

    ReservationResponseDTO cancelReservation(Long id);

    void deleteReservation(Long id);
}