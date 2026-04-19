package com.example.demo.dto;

import java.math.BigDecimal;

public record OperationRequest(
        Long compteId,
        BigDecimal montant,
        String description
) {}