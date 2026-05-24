package com.renteasy.renteasy.service.impl;
import com.renteasy.renteasy.dto.request.LogementRequestDTO;
import com.renteasy.renteasy.dto.response.LogementResponseDTO;
import com.renteasy.renteasy.entity.Logement;
import com.renteasy.renteasy.mapper.LogementMapper;
import com.renteasy.renteasy.repository.LogementRepository;
import com.renteasy.renteasy.service.LogementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LogementServiceImpl implements LogementService {

    private final LogementRepository logementRepository;

    @Override
    public LogementResponseDTO createLogement(LogementRequestDTO dto) {

        Logement logement = LogementMapper.toEntity(dto);

        Logement saved = logementRepository.save(logement);

        return LogementMapper.toResponse(saved);
    }

    @Override
    public List<LogementResponseDTO> getAllLogements() {

        return logementRepository.findAll()
                .stream()
                .map(LogementMapper::toResponse)
                .toList();
    }

    @Override
    public LogementResponseDTO getLogementById(Long id) {

        Logement logement = logementRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Logement not found")
                );

        return LogementMapper.toResponse(logement);
    }

    @Override
    public LogementResponseDTO updateLogement(
            Long id,
            LogementRequestDTO dto
    ) {

        Logement logement = logementRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Logement not found")
                );

        logement.setTitre(dto.getTitre());
        logement.setDescription(dto.getDescription());
        logement.setVille(dto.getVille());
        logement.setType(dto.getType());
        logement.setPrix(dto.getPrix());
        logement.setDisponible(dto.isDisponible());

        Logement updated = logementRepository.save(logement);

        return LogementMapper.toResponse(updated);
    }

    @Override
    public void deleteLogement(Long id) {

        logementRepository.deleteById(id);
    }
}