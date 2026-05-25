package com.renteasy.renteasy.service;



import com.renteasy.renteasy.entity.User;
import com.renteasy.renteasy.exception.ResourceNotFoundException;
import com.renteasy.renteasy.repository.UserRepository;
import com.renteasy.renteasy.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found")
                );

        return new CustomUserDetails(user);
    }
}
