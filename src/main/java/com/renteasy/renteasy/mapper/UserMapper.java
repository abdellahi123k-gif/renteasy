package com.renteasy.renteasy.mapper;



import com.renteasy.renteasy.dto.request.UserRequestDTO;
import com.renteasy.renteasy.dto.response.UserResponseDTO;
import com.renteasy.renteasy.entity.User;

public class UserMapper {

    public static User toEntity(UserRequestDTO dto) {

        return User.builder()
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .build();
    }

    public static UserResponseDTO toResponse(User user) {

        return UserResponseDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole().getName())
                .build();
    }
}