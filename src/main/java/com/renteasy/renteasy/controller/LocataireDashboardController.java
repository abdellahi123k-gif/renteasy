package com.renteasy.renteasy.controller;

import com.renteasy.renteasy.dto.ApiResponse;
import com.renteasy.renteasy.dto.Dashboard.LocataireDashboardDTO;
import com.renteasy.renteasy.service.LocataireDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LocataireDashboardController {

    private final LocataireDashboardService locataireDashboardService;

    @GetMapping("/api/locataire/dashboard")
    public ResponseEntity<ApiResponse<LocataireDashboardDTO>> locataireDashboard(Authentication authentication) {
        LocataireDashboardDTO response = locataireDashboardService.getDashboard(authentication.getName());
        return ResponseEntity.ok(ApiResponse.success("Dashboard retrieved successfully", response));
    }
}
