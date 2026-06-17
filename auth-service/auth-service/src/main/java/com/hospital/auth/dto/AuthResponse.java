package com.hospital.auth.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String username; // Optional: Frontend par welcome message dikhane ke liye
    private String role;     // Optional: Frontend par conditional routing ke liye
}