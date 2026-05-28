package com.billgate.backend.dto;

// DTO returned after successful login.
//
// We return only the JWT token,
// not the full User entity.
public class AuthResponse {

    public String token;

    public AuthResponse(String token) {
        this.token = token;
    }
}