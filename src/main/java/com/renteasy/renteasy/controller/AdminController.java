package com.renteasy.renteasy.controller;

import com.renteasy.renteasy.dto.Dashboard.AdminDashboardDTO;
import com.renteasy.renteasy.service.AdminService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/dashboard")
    public AdminDashboardDTO dashboard() {
        return adminService.getDashboard();
    }
}