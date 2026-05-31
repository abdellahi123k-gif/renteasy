package com.renteasy.renteasy.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ReservationRequestDTO {

    @NotNull(message = "Start date is required")
    @Future(message = "Start date must be in the future")
    private LocalDate dateDebut;

    @NotNull(message = "End date is required")
    @Future(message = "End date must be in the future")
    private LocalDate dateFin;

    @NotNull(message = "Logement id is required")
    private Long logementId;
}