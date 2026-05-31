package com.renteasy.renteasy.mapper;
import com.renteasy.renteasy.dto.request.LogementRequestDTO;
import com.renteasy.renteasy.dto.response.LogementResponseDTO;
import com.renteasy.renteasy.entity.Logement;

public class LogementMapper {

    public static Logement toEntity(LogementRequestDTO dto) {

        return Logement.builder()
                .titre(dto.getTitre())
                .description(dto.getDescription())
                .ville(dto.getVille())
                .adresse(dto.getAdresse())
                .type(dto.getType())
                .prix(dto.getPrix())
                .disponible(dto.isDisponible())
                .telephone(dto.getTelephone())
                .imageUrl(dto.getImageUrl())
                .videoUrl(dto.getVideoUrl())
                .build();
    }

    public static LogementResponseDTO toResponse(Logement logement) {

        return LogementResponseDTO.builder()
                .id(logement.getId())
                .titre(logement.getTitre())
                .description(logement.getDescription())
                .ville(logement.getVille())
                .adresse(logement.getAdresse())
                .type(logement.getType())
                .prix(logement.getPrix())
                .disponible(logement.isDisponible())
                .imageUrl(logement.getImageUrl())
                .videoUrl(logement.getVideoUrl())
                .telephone(logement.getTelephone())
                .ownerId(logement.getOwner() != null ? logement.getOwner().getId() : null)
                .ownerName(logement.getOwner() != null ?
                        logement.getOwner().getFirstName() + " " + logement.getOwner().getLastName() : null)
                .build();
    }
}