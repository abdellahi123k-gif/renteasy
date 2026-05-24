package com.renteasy.renteasy.dto.response;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class LogementResponseDTO {

    private Long id;

    private String titre;

    private String description;

    private String ville;

    private String type;

    private BigDecimal prix;

    private boolean disponible;

    private String ownerName;
}