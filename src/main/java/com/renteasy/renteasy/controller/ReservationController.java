package com.renteasy.renteasy.controller;

import com.renteasy.renteasy.dto.request.ReservationRequestDTO;
import com.renteasy.renteasy.dto.response.ReservationResponseDTO;
import com.renteasy.renteasy.service.ReservationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    public ReservationResponseDTO createReservation(
            @Valid @RequestBody ReservationRequestDTO dto
    ) {

        return reservationService.createReservation(dto);
    }

    @GetMapping
    public List<ReservationResponseDTO> getAllReservations() {

        return reservationService.getAllReservations();
    }
}