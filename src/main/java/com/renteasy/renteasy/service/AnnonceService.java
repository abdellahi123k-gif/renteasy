package com.renteasy.renteasy.service;

import com.renteasy.renteasy.dto.request.AnnonceRequestDTO;
import com.renteasy.renteasy.dto.response.AnnonceResponseDTO;

import java.util.List;

public interface AnnonceService {

    AnnonceResponseDTO createAnnonce(
            AnnonceRequestDTO dto
    );

    List<AnnonceResponseDTO> getAllAnnonces();

    List<AnnonceResponseDTO> getActiveAnnonces();
}