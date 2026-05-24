package com.renteasy.renteasy.controller;
import com.renteasy.renteasy.dto.request.LogementRequestDTO;
import com.renteasy.renteasy.dto.response.LogementResponseDTO;
import com.renteasy.renteasy.service.LogementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logements")
@RequiredArgsConstructor
public class LogementController {

    private final LogementService logementService;

    @PostMapping
    public LogementResponseDTO create(
            @Valid @RequestBody LogementRequestDTO dto
    ) {

        return logementService.createLogement(dto);
    }

    @GetMapping
    public List<LogementResponseDTO> getAll() {

        return logementService.getAllLogements();
    }

    @GetMapping("/{id}")
    public LogementResponseDTO getById(@PathVariable Long id) {

        return logementService.getLogementById(id);
    }

    @PutMapping("/{id}")
    public LogementResponseDTO update(
            @PathVariable Long id,
            @Valid @RequestBody LogementRequestDTO dto
    ) {

        return logementService.updateLogement(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {

        logementService.deleteLogement(id);
    }
}