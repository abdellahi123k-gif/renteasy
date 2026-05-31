package com.renteasy.renteasy.controller;

import com.renteasy.renteasy.dto.ApiResponse;
import com.renteasy.renteasy.dto.request.AnnonceRequestDTO;
import com.renteasy.renteasy.dto.response.AnnonceResponseDTO;
import com.renteasy.renteasy.service.AnnonceService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/annonces")
@RequiredArgsConstructor
public class AnnonceController {

    private final AnnonceService annonceService;

    @PostMapping
    public ResponseEntity<ApiResponse<AnnonceResponseDTO>> createAnnonce(
            @Valid @RequestBody AnnonceRequestDTO dto
    ) {
        AnnonceResponseDTO response = annonceService.createAnnonce(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Annonce created successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AnnonceResponseDTO>>> getAllAnnonces() {
        List<AnnonceResponseDTO> response = annonceService.getAllAnnonces();
        return ResponseEntity.ok(ApiResponse.success("Annonces retrieved successfully", response));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<AnnonceResponseDTO>>> getActiveAnnonces() {
        List<AnnonceResponseDTO> response = annonceService.getActiveAnnonces();
        return ResponseEntity.ok(ApiResponse.success("Active annonces retrieved successfully", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AnnonceResponseDTO>> getAnnonceById(@PathVariable Long id) {
        AnnonceResponseDTO response = annonceService.getAnnonceById(id);
        return ResponseEntity.ok(ApiResponse.success("Annonce retrieved successfully", response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AnnonceResponseDTO>> updateAnnonce(
            @PathVariable Long id,
            @Valid @RequestBody AnnonceRequestDTO dto
    ) {
        AnnonceResponseDTO response = annonceService.updateAnnonce(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Annonce updated successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnnonce(@PathVariable Long id) {
        annonceService.deleteAnnonce(id);
        return ResponseEntity.noContent().build();
    }
}
