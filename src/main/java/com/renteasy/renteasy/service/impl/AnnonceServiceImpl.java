package com.renteasy.renteasy.service.impl;

import com.renteasy.renteasy.dto.request.AnnonceRequestDTO;
import com.renteasy.renteasy.dto.response.AnnonceResponseDTO;
import com.renteasy.renteasy.entity.Annonce;
import com.renteasy.renteasy.entity.Logement;
import com.renteasy.renteasy.exception.ResourceNotFoundException;
import com.renteasy.renteasy.mapper.AnnonceMapper;
import com.renteasy.renteasy.repository.AnnonceRepository;
import com.renteasy.renteasy.repository.LogementRepository;
import com.renteasy.renteasy.service.AnnonceService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnnonceServiceImpl implements AnnonceService {

    private final AnnonceRepository annonceRepository;
    private final LogementRepository logementRepository;

    @Override
    public AnnonceResponseDTO createAnnonce(AnnonceRequestDTO dto) {

        // 1. Vérifier logement existe
        Logement logement = logementRepository.findById(dto.getLogementId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Logement not found")
                );

        // 2. Business Rule: logement يجب أن يكون disponible
        if (!logement.isDisponible()) {
            throw new RuntimeException("Cannot publish annonce for unavailable logement");
        }

        // 3. Create annonce
        Annonce annonce = Annonce.builder()
                .titre(dto.getTitre())
                .description(dto.getDescription())
                .active(true) // default published
                .datePublication(LocalDateTime.now())
                .logement(logement)
                .build();

        // 4. Save
        Annonce saved = annonceRepository.save(annonce);

        return AnnonceMapper.toResponse(saved);
    }

    @Override
    public List<AnnonceResponseDTO> getAllAnnonces() {

        return annonceRepository.findAll()
                .stream()
                .map(AnnonceMapper::toResponse)
                .toList();
    }

    @Override
    public List<AnnonceResponseDTO> getActiveAnnonces() {

        return annonceRepository.findByActiveTrue()
                .stream()
                .map(AnnonceMapper::toResponse)
                .toList();
    }
}