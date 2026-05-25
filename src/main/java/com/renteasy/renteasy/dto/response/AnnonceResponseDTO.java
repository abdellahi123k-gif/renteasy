package com.renteasy.renteasy.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AnnonceResponseDTO {

    private Long id;

    private String titre;

    private String description;

    private boolean active;

    private LocalDateTime datePublication;

    private String logementTitre;
}