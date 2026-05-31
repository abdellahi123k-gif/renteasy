package com.renteasy.renteasy.controller;

import com.renteasy.renteasy.dto.ApiResponse;
import com.renteasy.renteasy.dto.request.LogementRequestDTO;
import com.renteasy.renteasy.dto.response.LogementResponseDTO;
import com.renteasy.renteasy.service.LogementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/logements")
@RequiredArgsConstructor
public class LogementController {

    private final LogementService logementService;

    @PostMapping
    public ResponseEntity<ApiResponse<LogementResponseDTO>> create(
            @Valid @RequestBody LogementRequestDTO dto
    ) {
        LogementResponseDTO response = logementService.createLogement(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Logement created successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<LogementResponseDTO>>> getAll(Pageable pageable) {
        Page<LogementResponseDTO> response = logementService.getAllLogements(pageable);
        return ResponseEntity.ok(ApiResponse.success("Logements retrieved successfully", response));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<LogementResponseDTO>>> search(
            @RequestParam(required = false) String ville,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) BigDecimal minPrix,
            @RequestParam(required = false) BigDecimal maxPrix,
            @RequestParam(required = false) Boolean disponible,
            Pageable pageable
    ) {
        Page<LogementResponseDTO> response = logementService.searchLogements(
                ville, type, minPrix, maxPrix, disponible, pageable
        );
        return ResponseEntity.ok(ApiResponse.success("Logements retrieved successfully", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LogementResponseDTO>> getById(@PathVariable Long id) {
        LogementResponseDTO response = logementService.getLogementById(id);
        return ResponseEntity.ok(ApiResponse.success("Logement retrieved successfully", response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<LogementResponseDTO>> update(
            @PathVariable Long id,
            @Valid @RequestBody LogementRequestDTO dto
    ) {
        LogementResponseDTO response = logementService.updateLogement(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Logement updated successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logementService.deleteLogement(id);
        return ResponseEntity.noContent().build();
    }
}
