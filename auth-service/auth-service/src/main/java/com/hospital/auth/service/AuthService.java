package com.hospital.auth.service;

import com.hospital.auth.dto.AuthRequest;
import com.hospital.auth.dto.AuthResponse;
import com.hospital.auth.entity.User;
import com.hospital.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 1. Login Method (Returning formatted text token without JwtUtil dependency)
    public Optional<AuthResponse> authenticate(AuthRequest request) {
        Optional<User> userOpt = userRepository.findByUsername(request.getUsername());

        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // Safe BCrypt password verification
            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {

                // 🚀 Removed JwtUtil dependency and rolled back to simple token string format
                String token = "mock-jwt-token-for-" + user.getUsername() + "-" + user.getRole();

                AuthResponse response = new AuthResponse(
                        token,
                        user.getUsername(),
                        user.getRole()
                );
                return Optional.of(response);
            }
        }
        return Optional.empty();
    }

    // 2. Registration Method (Saves encrypted passwords to DB)
    public User register(User user) {
        // Password ko save karne se pehle BCrypt hash me convert kar rahe hain
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}