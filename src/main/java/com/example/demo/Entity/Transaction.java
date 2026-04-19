package com.example.demo.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal montant;

    @Column(nullable = false)
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "compte_id", nullable = false)
    private Account compte;

    private String description;

    public Transaction(String type, BigDecimal montant, Account compte, String description) {
        this.type = type;
        this.montant = montant;
        this.compte = compte;
        this.description = description;
        this.date = LocalDateTime.now();
    }
}