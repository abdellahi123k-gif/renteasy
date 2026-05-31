package com.renteasy.renteasy.mapper;

import com.renteasy.renteasy.dto.response.AnnonceResponseDTO;
import com.renteasy.renteasy.entity.Annonce;

public class AnnonceMapper {

    public static AnnonceResponseDTO toResponse(
            Annonce annonce
    ) {

        return AnnonceResponseDTO.builder()
                .id(annonce.getId())
                .titre(annonce.getTitre())
                .description(annonce.getDescription())
                .active(annonce.isActive())
                .datePublication(
                        annonce.getDatePublication()
                )
                .logementId(annonce.getLogement().getId())
                .logementTitre(
                        annonce.getLogement().getTitre()
                )
                .ownerId(annonce.getLogement().getOwner() != null ?
                        annonce.getLogement().getOwner().getId() : null)
                .build();
    }
}