package com.renteasy.renteasy.service.impl;

import com.renteasy.renteasy.dto.request.AnnonceRequestDTO;
import com.renteasy.renteasy.dto.response.AnnonceResponseDTO;
import com.renteasy.renteasy.entity.Annonce;
import com.renteasy.renteasy.entity.Logement;
import com.renteasy.renteasy.entity.User;
import com.renteasy.renteasy.exception.LogementNotAvailableException;
import com.renteasy.renteasy.exception.ResourceNotFoundException;
import com.renteasy.renteasy.exception.UnauthorizedActionException;
import com.renteasy.renteasy.mapper.AnnonceMapper;
import com.renteasy.renteasy.repository.AnnonceRepository;
import com.renteasy.renteasy.repository.LogementRepository;
import com.renteasy.renteasy.security.SecurityUtils;
import com.renteasy.renteasy.service.AnnonceService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnnonceServiceImpl implements AnnonceService {

    private final AnnonceRepository annonceRepository;
    private final LogementRepository logementRepository;
    private final SecurityUtils securityUtils;

    @Override
    @Transactional
    public AnnonceResponseDTO createAnnonce(AnnonceRequestDTO dto) {
        Logement logement = logementRepository.findById(dto.getLogementId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Logement not found"));
        User currentUser = securityUtils.getCurrentUser();
        if (!logement.getOwner().getId().equals(currentUser.getId())) {
            throw new UnauthorizedActionException("You can only create annonces for your own logements");
        }
        if (!logement.isDisponible()) {
            throw new LogementNotAvailableException("Cannot publish annonce for unavailable logement");
        }
        Annonce annonce = Annonce.builder()
                .titre(dto.getTitre())
                .description(dto.getDescription())
                .active(true)
                .datePublication(LocalDateTime.now())
                .logement(logement)
                .build();
        Annonce saved = annonceRepository.save(annonce);
        return AnnonceMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AnnonceResponseDTO> getAllAnnonces() {
        return annonceRepository.findAll()
                .stream()
                .map(AnnonceMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AnnonceResponseDTO> getActiveAnnonces() {
        return annonceRepository.findByActiveTrue()
                .stream()
                .map(AnnonceMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AnnonceResponseDTO getAnnonceById(Long id) {
        Annonce annonce = annonceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Annonce not found"));
        return AnnonceMapper.toResponse(annonce);
    }

    @Override
    @Transactional
    public AnnonceResponseDTO updateAnnonce(Long id, AnnonceRequestDTO dto) {
        Annonce annonce = annonceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Annonce not found"));
        User currentUser = securityUtils.getCurrentUser();
        if (!annonce.getLogement().getOwner().getId().equals(currentUser.getId())) {
            throw new UnauthorizedActionException("You can only update annonces for your own logements");
        }
        annonce.setTitre(dto.getTitre());
        annonce.setDescription(dto.getDescription());
        if (dto.getLogementId() != null) {
            Logement logement = logementRepository.findById(dto.getLogementId())
                    .orElseThrow(() -> new ResourceNotFoundException("Logement not found"));
            annonce.setLogement(logement);
        }
        return AnnonceMapper.toResponse(annonce);
    }

    @Override
    @Transactional
    public void deleteAnnonce(Long id) {
        Annonce annonce = annonceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Annonce not found"));
        User currentUser = securityUtils.getCurrentUser();
        if (!annonce.getLogement().getOwner().getId().equals(currentUser.getId())) {
            throw new UnauthorizedActionException("You can only delete annonces for your own logements");
        }
        annonceRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AnnonceResponseDTO> getUserAnnonces() {
        User currentUser = securityUtils.getCurrentUser();
        return annonceRepository.findByLogementOwnerId(currentUser.getId())
                .stream()
                .map(AnnonceMapper::toResponse)
                .toList();
    }
}
