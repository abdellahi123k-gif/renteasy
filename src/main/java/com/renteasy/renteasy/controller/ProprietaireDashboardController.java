package com.renteasy.renteasy.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProprietaireDashboardController {

    @GetMapping("/api/proprietaire/dashboard")
    public String proprietaireDashboard() {

        return "Welcome Proprietaire Dashboard";
    }
}