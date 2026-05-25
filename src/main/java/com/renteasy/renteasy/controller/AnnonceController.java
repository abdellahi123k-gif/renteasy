package com.renteasy.renteasy.controller;

import com.renteasy.renteasy.dto.request.AnnonceRequestDTO;
import com.renteasy.renteasy.dto.response.AnnonceResponseDTO;
import com.renteasy.renteasy.service.AnnonceService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/annonces")
@RequiredArgsConstructor
public class AnnonceController {

    private final AnnonceService annonceService;

    @PostMapping
    public AnnonceResponseDTO createAnnonce(
            @Valid @RequestBody AnnonceRequestDTO dto
    ) {
        return annonceService.createAnnonce(dto);
    }

    @GetMapping
    public List<AnnonceResponseDTO> getAllAnnonces() {
        return annonceService.getAllAnnonces();
    }

    @GetMapping("/active")
    public List<AnnonceResponseDTO> getActiveAnnonces() {
        return annonceService.getActiveAnnonces();
    }
}