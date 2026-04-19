package com.example.demo.dto;

public record RegisterRequest(
        String nom,
        String prenom,
        String email,
        String motDePasse,
        String telephone
) {}