package com.renteasy.renteasy.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AnnonceRequestDTO {

    @NotBlank(message = "Title is required")
    private String titre;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Logement id is required")
    private Long logementId;
}