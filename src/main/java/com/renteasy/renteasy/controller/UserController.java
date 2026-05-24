package com.renteasy.renteasy.controller;


import com.renteasy.renteasy.dto.request.UserRequestDTO;
import com.renteasy.renteasy.dto.response.UserResponseDTO;
import com.renteasy.renteasy.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserResponseDTO createUser(
            @Valid @RequestBody UserRequestDTO requestDTO
    ) {

        return userService.createUser(requestDTO);
    }
}
