package com.renteasy.renteasy.service.impl;

import com.renteasy.renteasy.dto.request.UserRequestDTO;
import com.renteasy.renteasy.dto.response.UserResponseDTO;
import com.renteasy.renteasy.entity.Role;
import com.renteasy.renteasy.entity.User;
import com.renteasy.renteasy.exception.ResourceNotFoundException;
import com.renteasy.renteasy.exception.UnauthorizedActionException;
import com.renteasy.renteasy.mapper.UserMapper;
import com.renteasy.renteasy.repository.RoleRepository;
import com.renteasy.renteasy.repository.UserRepository;
import com.renteasy.renteasy.security.SecurityUtils;
import com.renteasy.renteasy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final SecurityUtils securityUtils;

    @Override
    @Transactional
    public UserResponseDTO createUser(UserRequestDTO requestDTO) {
        User currentUser = securityUtils.getCurrentUser();
        boolean isAdmin = currentUser.getRole().getName().equals("ADMIN");
        if (!isAdmin) {
            throw new UnauthorizedActionException("Only admins can create users");
        }
        String roleName = requestDTO.getRole() != null ? requestDTO.getRole().toUpperCase() : "LOCATAIRE";
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() ->
                        new RuntimeException("Role " + roleName + " not found"));
        User user = UserMapper.toEntity(requestDTO);
        user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        user.setRole(role);
        User savedUser = userRepository.save(user);
        return UserMapper.toResponse(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));
        return UserMapper.toResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User currentUser = securityUtils.getCurrentUser();
        boolean isAdmin = currentUser.getRole().getName().equals("ADMIN");
        if (!isAdmin) {
            throw new UnauthorizedActionException("Only admins can delete users");
        }
        if (currentUser.getId().equals(id)) {
            throw new IllegalArgumentException("Cannot delete your own account");
        }
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userRepository.delete(user);
    }
}
