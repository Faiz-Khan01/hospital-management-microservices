package com.hospital.auth.controller;

import com.hospital.auth.dto.AuthRequest;
import com.hospital.auth.dto.AuthResponse;
import com.hospital.auth.entity.User;
import com.hospital.auth.repository.UserRepository;
import com.hospital.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository; // Needed to check existing users

    // 1. Existing Login Endpoint
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        Optional<AuthResponse> authResponse = authService.authenticate(request);

        if (authResponse.isPresent()) {
            return ResponseEntity.ok(authResponse.get());
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Invalid Username or Password");
    }

    // 2. New Registration Endpoint (Add this inside the class)
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        // Validation: Check if username already exists in DB
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already taken!");
        }

        User savedUser = authService.register(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }
}