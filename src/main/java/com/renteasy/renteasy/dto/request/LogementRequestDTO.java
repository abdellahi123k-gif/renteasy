package com.renteasy.renteasy.dto.request;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Data
public class LogementRequestDTO {

    @NotBlank(message = "Title is required")
    private String titre;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "City is required")
    private String ville;
    private String adresse;
    @NotBlank(message = "Type is required")
    private String type;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private BigDecimal prix;

    private boolean disponible;

    private String telephone;

    private String imageUrl;

    private String videoUrl;

    private MultipartFile imageFile;

    private MultipartFile videoFile;
}
