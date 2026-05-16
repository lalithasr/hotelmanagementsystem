package com.example.hotelmanagementsystem.service;

import com.example.hotelmanagementsystem.dto.AuthResponseDto;
import com.example.hotelmanagementsystem.dto.LoginRequestDto;
import com.example.hotelmanagementsystem.dto.RegisterRequestDto;
import com.example.hotelmanagementsystem.entity.User;
import com.example.hotelmanagementsystem.exception.EmailAlreadyExistsException;
import com.example.hotelmanagementsystem.repository.UserRepository;
import com.example.hotelmanagementsystem.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthResponseDto register(RegisterRequestDto dto) {
        log.info("AuthService::register - Registering user with email: {}", dto.getEmail());

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyExistsException("Email already registered: " + dto.getEmail());
        }

        User user = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(dto.getRole() != null ? dto.getRole().toUpperCase() : "USER")
                .build();

        userRepository.save(user);
        String token = jwtUtil.generateToken(user.getEmail());

        log.info("AuthService::register - User registered successfully: {}", dto.getEmail());

        return AuthResponseDto.builder()
                .token(token)
                .email(user.getEmail())
                .role(user.getRole())
                .message("Registration successful")
                .build();
    }

    public AuthResponseDto login(LoginRequestDto dto) {
        log.info("AuthService::login - Login attempt for: {}", dto.getEmail());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow();

        String token = jwtUtil.generateToken(user.getEmail());

        log.info("AuthService::login - Login successful for: {}", dto.getEmail());

        return AuthResponseDto.builder()
                .token(token)
                .email(user.getEmail())
                .role(user.getRole())
                .message("Login successful")
                .build();
    }
}

