package com.example.demo.controller;

import com.example.demo.Entity.Account;
import com.example.demo.Entity.Transaction;
import com.example.demo.dto.OperationRequest;
import com.example.demo.dto.VirementRequest;
import com.example.demo.service.Iservice.ITransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/operations")
@RequiredArgsConstructor
@Tag(name = "6.4 Transactions", description = "Opérations bancaires - Token requis")
public class OperationController {

    private final ITransactionService transactionService;

    @PostMapping("/depot")
    @Operation(summary = "Dépôt", description = "Effectuer un dépôt d'argent sur un compte. Indiquez l'ID du compte et le montant à déposer.")
    public ResponseEntity<Transaction> depot(@RequestBody OperationRequest request) {
        Transaction tx = transactionService.depot(request.compteId(), request.montant(), request.description());
        return new ResponseEntity<>(tx, HttpStatus.CREATED);
    }

    @PostMapping("/retrait")
    @Operation(summary = "Retrait", description = "Effectuer un retrait d'argent sur un compte. Le solde doit être suffisant. Indiquez l'ID du compte et le montant à retirer.")
    public ResponseEntity<Transaction> retrait(@RequestBody OperationRequest request) {
        Transaction tx = transactionService.retrait(request.compteId(), request.montant(), request.description());
        return new ResponseEntity<>(tx, HttpStatus.CREATED);
    }

    @PostMapping("/virement")
    @Operation(summary = "Virement", description = "Transférer de l'argent d'un compte à un autre. Indiquez le compte source, le compte destinataire et le montant.")
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
    @Operation(summary = "Relevé d'opérations", description = "Affiche l'historique de toutes les opérations (dépôts, retraits, virements) d'un compte.")
    public ResponseEntity<List<Transaction>> releve(@PathVariable Long compteId) {
        return ResponseEntity.ok(transactionService.getReleve(compteId));
    }

    @GetMapping("/{compteId}/solde")
    @Operation(summary = "Solde actuel", description = "Affiche le solde actuel d'un compte bancaire.")
    public ResponseEntity<Map<String, Object>> getSolde(@PathVariable Long compteId) {
        Account compte = transactionService.getSolde(compteId);
        return ResponseEntity.ok(Map.of(
                "compteId", compte.getId(),
                "solde", compte.getSolde(),
                "currency", compte.getCurrency()
        ));
    }
}