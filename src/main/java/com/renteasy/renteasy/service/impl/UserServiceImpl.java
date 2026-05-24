package com.renteasy.renteasy.service.impl;


import com.renteasy.renteasy.dto.request.UserRequestDTO;
import com.renteasy.renteasy.dto.response.UserResponseDTO;
import com.renteasy.renteasy.entity.Role;
import com.renteasy.renteasy.entity.User;
import com.renteasy.renteasy.mapper.UserMapper;
import com.renteasy.renteasy.repository.RoleRepository;
import com.renteasy.renteasy.repository.UserRepository;
import com.renteasy.renteasy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public UserResponseDTO createUser(UserRequestDTO requestDTO) {
        Role role = roleRepository.findByName("LOCATAIRE")
                .orElseThrow(() ->
                        new RuntimeException("Role LOCATAIRE not found")
                );

        User user = UserMapper.toEntity(requestDTO);

        user.setPassword(
                passwordEncoder.encode(requestDTO.getPassword())
        );

        user.setRole(role);

        User savedUser = userRepository.save(user);

        return UserMapper.toResponse(savedUser);
    }
}