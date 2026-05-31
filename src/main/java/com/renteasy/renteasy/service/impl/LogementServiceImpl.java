package com.renteasy.renteasy.service.impl;
import com.renteasy.renteasy.dto.request.LogementRequestDTO;
import com.renteasy.renteasy.dto.response.LogementResponseDTO;
import com.renteasy.renteasy.entity.Logement;
import com.renteasy.renteasy.entity.User;
import com.renteasy.renteasy.exception.ResourceNotFoundException;
import com.renteasy.renteasy.exception.UnauthorizedActionException;
import com.renteasy.renteasy.mapper.LogementMapper;
import com.renteasy.renteasy.repository.LogementRepository;
import com.renteasy.renteasy.security.SecurityUtils;
import com.renteasy.renteasy.service.LogementService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class LogementServiceImpl implements LogementService {

    private final LogementRepository logementRepository;
    private final SecurityUtils securityUtils;

    @Override
    @Transactional
    public LogementResponseDTO createLogement(LogementRequestDTO dto) {
        User owner = securityUtils.getCurrentUser();
        Logement logement = LogementMapper.toEntity(dto);
        logement.setOwner(owner);
        Logement saved = logementRepository.save(logement);
        return LogementMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LogementResponseDTO> getAllLogements(Pageable pageable) {
        return logementRepository.findAll(pageable)
                .map(LogementMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public LogementResponseDTO getLogementById(Long id) {
        Logement logement = logementRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Logement not found"));
        return LogementMapper.toResponse(logement);
    }

    @Override
    @Transactional
    public LogementResponseDTO updateLogement(Long id, LogementRequestDTO dto) {
        Logement logement = logementRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Logement not found"));
        User currentUser = securityUtils.getCurrentUser();
        if (!logement.getOwner().getId().equals(currentUser.getId())) {
            throw new UnauthorizedActionException("You can only update your own logements");
        }
        logement.setTitre(dto.getTitre());
        logement.setDescription(dto.getDescription());
        logement.setVille(dto.getVille());
        logement.setAdresse(dto.getAdresse());
        logement.setType(dto.getType());
        logement.setPrix(dto.getPrix());
        logement.setDisponible(dto.isDisponible());
        logement.setTelephone(dto.getTelephone());
        logement.setImageUrl(dto.getImageUrl());
        logement.setVideoUrl(dto.getVideoUrl());
        return LogementMapper.toResponse(logement);
    }

    @Override
    @Transactional
    public void deleteLogement(Long id) {
        Logement logement = logementRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Logement not found"));
        User currentUser = securityUtils.getCurrentUser();
        if (!logement.getOwner().getId().equals(currentUser.getId())) {
            throw new UnauthorizedActionException("You can only delete your own logements");
        }
        logementRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LogementResponseDTO> searchLogements(
            String ville, String type, BigDecimal minPrix,
            BigDecimal maxPrix, Boolean disponible, Pageable pageable) {
        return logementRepository.search(ville, type, minPrix, maxPrix, disponible, pageable)
                .map(LogementMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<LogementResponseDTO> getUserLogements(Pageable pageable) {
        User currentUser = securityUtils.getCurrentUser();
        return logementRepository.findByOwner_Id(currentUser.getId(), pageable)
                .map(LogementMapper::toResponse);
    }
}
