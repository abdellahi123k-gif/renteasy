package com.renteasy.renteasy.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LocataireDashboardController {

    @GetMapping("/api/locataire/dashboard")
    public String locataireDashboard() {
        return "Welcome Locataire Dashboard";
    }
}