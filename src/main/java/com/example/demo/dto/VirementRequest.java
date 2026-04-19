package com.example.demo.dto;

import java.math.BigDecimal;

public record VirementRequest(
        Long compteSource,
        Long compteDestinataire,
        BigDecimal montant,
        String description
) {}