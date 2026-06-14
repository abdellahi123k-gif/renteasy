package com.renteasy.renteasy.service;
import com.renteasy.renteasy.dto.request.LogementRequestDTO;
import com.renteasy.renteasy.dto.response.LogementResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface LogementService {

    LogementResponseDTO createLogement(LogementRequestDTO dto);

    Page<LogementResponseDTO> getAllLogements(Pageable pageable);

    LogementResponseDTO getLogementById(Long id);

    LogementResponseDTO updateLogement(Long id, LogementRequestDTO dto);

    void deleteLogement(Long id);

    Page<LogementResponseDTO> searchLogements(
            String ville, String type, BigDecimal minPrix,
            BigDecimal maxPrix, Boolean disponible, Pageable pageable
    );

    Page<LogementResponseDTO> getUserLogements(Pageable pageable);

    Page<LogementResponseDTO> getUserDisponibleLogements(Pageable pageable);
}