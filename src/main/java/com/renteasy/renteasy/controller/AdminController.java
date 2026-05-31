package com.renteasy.renteasy.controller;

import com.renteasy.renteasy.dto.ApiResponse;
import com.renteasy.renteasy.dto.Dashboard.AdminDashboardDTO;
import com.renteasy.renteasy.dto.response.UserResponseDTO;
import com.renteasy.renteasy.service.AdminService;
import com.renteasy.renteasy.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final UserService userService;

    @GetMapping("/dashboard")
    public ResponseEntity<ApiResponse<AdminDashboardDTO>> dashboard() {
        AdminDashboardDTO response = adminService.getDashboard();
        return ResponseEntity.ok(ApiResponse.success("Dashboard retrieved successfully", response));
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserResponseDTO>>> getAllUsers() {
        List<UserResponseDTO> response = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", response));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
