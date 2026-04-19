package com.example.demo.dto;

public record AuthResponse(
        String token,
        String type,
        String email,
        Long clientId,
        String message
) {}