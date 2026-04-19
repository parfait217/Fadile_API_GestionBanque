package com.example.demo.controller;

import com.example.demo.Entity.Account;
import com.example.demo.Entity.Transaction;
import com.example.demo.dto.OperationRequest;
import com.example.demo.dto.VirementRequest;
import com.example.demo.service.Iservice.ITransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/operations")
@RequiredArgsConstructor
public class OperationController {

    private final ITransactionService transactionService;

    @PostMapping("/depot")
    public ResponseEntity<Transaction> depot(@RequestBody OperationRequest request) {
        Transaction tx = transactionService.depot(request.compteId(), request.montant(), request.description());
        return new ResponseEntity<>(tx, HttpStatus.CREATED);
    }

    @PostMapping("/retrait")
    public ResponseEntity<Transaction> retrait(@RequestBody OperationRequest request) {
        Transaction tx = transactionService.retrait(request.compteId(), request.montant(), request.description());
        return new ResponseEntity<>(tx, HttpStatus.CREATED);
    }

    @PostMapping("/virement")
    public ResponseEntity<Transaction> virement(@RequestBody VirementRequest request) {
        Transaction tx = transactionService.virement(
                request.compteSource(),
                request.compteDestinataire(),
                request.montant(),
                request.description()
        );
        return new ResponseEntity<>(tx, HttpStatus.CREATED);
    }

    @GetMapping("/releve/{compteId}")
    public ResponseEntity<List<Transaction>> releve(@PathVariable Long compteId) {
        return ResponseEntity.ok(transactionService.getReleve(compteId));
    }

    @GetMapping("/{compteId}/solde")
    public ResponseEntity<Map<String, Object>> getSolde(@PathVariable Long compteId) {
        Account compte = transactionService.getSolde(compteId);
        return ResponseEntity.ok(Map.of(
                "compteId", compte.getId(),
                "solde", compte.getSolde(),
                "currency", compte.getCurrency()
        ));
    }
}