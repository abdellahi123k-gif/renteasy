package com.renteasy.renteasy.service;

import com.renteasy.renteasy.dto.request.UserRequestDTO;
import com.renteasy.renteasy.dto.response.UserResponseDTO;

import java.util.List;

public interface UserService {

    UserResponseDTO createUser(UserRequestDTO requestDTO);

    UserResponseDTO getUserByEmail(String email);

    List<UserResponseDTO> getAllUsers();

    void deleteUser(Long id);
}
