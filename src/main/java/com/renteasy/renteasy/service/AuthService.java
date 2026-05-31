package com.renteasy.renteasy.service;

import com.renteasy.renteasy.dto.request.LoginRequest;
import com.renteasy.renteasy.dto.request.RegisterRequest;
import com.renteasy.renteasy.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}
