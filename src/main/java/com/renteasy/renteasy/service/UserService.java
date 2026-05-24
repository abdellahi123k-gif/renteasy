package com.renteasy.renteasy.service;



import com.renteasy.renteasy.dto.request.UserRequestDTO;
import com.renteasy.renteasy.dto.response.UserResponseDTO;

public interface UserService {

    UserResponseDTO createUser(UserRequestDTO requestDTO);
}
