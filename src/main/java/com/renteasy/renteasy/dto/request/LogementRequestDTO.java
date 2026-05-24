package com.renteasy.renteasy.dto.request;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class LogementRequestDTO {

    @NotBlank(message = "Title is required")
    private String titre;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "City is required")
    private String ville;

    @NotBlank(message = "Type is required")
    private String type;

    @Positive(message = "Price must be positive")
    private BigDecimal prix;

    private boolean disponible;
}
