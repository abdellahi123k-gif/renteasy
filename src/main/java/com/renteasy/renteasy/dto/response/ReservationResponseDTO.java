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

    private Long locataireId;

    private String locataireName;

    private Long logementId;

    private String logementTitre;
}