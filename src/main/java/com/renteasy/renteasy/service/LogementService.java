package com.renteasy.renteasy.service;
import com.renteasy.renteasy.dto.request.LogementRequestDTO;
import com.renteasy.renteasy.dto.response.LogementResponseDTO;

import java.util.List;

public interface LogementService {

    LogementResponseDTO createLogement(LogementRequestDTO dto);

    List<LogementResponseDTO> getAllLogements();

    LogementResponseDTO getLogementById(Long id);

    LogementResponseDTO updateLogement(Long id, LogementRequestDTO dto);

    void deleteLogement(Long id);
}