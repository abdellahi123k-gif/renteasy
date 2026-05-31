package com.renteasy.renteasy.controller;

import com.renteasy.renteasy.dto.ApiResponse;
import com.renteasy.renteasy.dto.Dashboard.ProprietaireDashboardDTO;
import com.renteasy.renteasy.service.ProprietaireDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProprietaireDashboardController {

    private final ProprietaireDashboardService proprietaireDashboardService;

    @GetMapping("/api/proprietaire/dashboard")
    public ResponseEntity<ApiResponse<ProprietaireDashboardDTO>> proprietaireDashboard(Authentication authentication) {
        ProprietaireDashboardDTO response = proprietaireDashboardService.getDashboard(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Dashboard retrieved successfully", response));
    }
}
