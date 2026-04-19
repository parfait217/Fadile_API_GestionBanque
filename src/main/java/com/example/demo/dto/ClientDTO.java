package com.example.demo.dto;

public record ClientDTO(
        Long id,
        String nom,
        String prenom,
        String email,
        String telephone
) {}