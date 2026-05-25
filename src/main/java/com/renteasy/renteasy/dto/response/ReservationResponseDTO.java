package com.renteasy.renteasy.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ReservationResponseDTO {

    private Long id;

    private LocalDate dateDebut;

    private LocalDate dateFin;

    private String status;

    private String locataireName;

    private String logementTitre;
}