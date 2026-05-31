package com.renteasy.renteasy.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {

    private String token;
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
}
