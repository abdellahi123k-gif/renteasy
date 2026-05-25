package com.renteasy.renteasy.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ReservationRequestDTO {

    @NotNull(message = "Start date is required")
    private LocalDate dateDebut;

    @NotNull(message = "End date is required")
    private LocalDate dateFin;

    @NotNull(message = "Locataire id is required")
    private Long locataireId;

    @NotNull(message = "Logement id is required")
    private Long logementId;
}