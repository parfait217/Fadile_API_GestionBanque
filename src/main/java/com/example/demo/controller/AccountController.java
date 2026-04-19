package com.example.demo.controller;

import com.example.demo.Entity.Account;
import com.example.demo.dto.CreateAccountRequest;
import com.example.demo.service.Iservice.IAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comptes")
@RequiredArgsConstructor
@Tag(name = "6.3 Comptes Bancaires", description = "Gestion des comptes - Token requis")
public class AccountController {

    private final IAccountService accountService;

    @PostMapping
    @Operation(summary = "Ouvrir un compte", description = "Créer un nouveau compte bancaire. Indiquez le nom du compte (ex: Compte épargne), la devise (XAF, EUR), et le solde initial.")
    public ResponseEntity<Account> createAccount(@RequestBody CreateAccountRequest request) {
        return new ResponseEntity<>(accountService.createAccount(request), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Liste des comptes", description = "Affiche la liste de tous les comptes bancaires existants.")
    public ResponseEntity<List<Account>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Détails compte", description = "Affiche les informations complètes d'un compte bancaire.")
    public ResponseEntity<Account> getAccountById(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.getAccountById(id));
    }

    @GetMapping("/client/{clientId}")
    @Operation(summary = "Comptes d'un client", description = "Liste tous les comptes bancaires appartenants à un client spécifique.")
    public ResponseEntity<List<Account>> getAccountsByClient(@PathVariable Long clientId) {
        return ResponseEntity.ok(accountService.getAccountsByClientId(clientId));
    }

    @GetMapping("/{compteId}/solde")
    @Operation(summary = "Consulter le solde", description = "Affiche le solde actuel d'un compte bancaire.")
    public ResponseEntity<Map<String, Object>> getSolde(@PathVariable Long compteId) {
        Account account = accountService.getAccountById(compteId);
        return ResponseEntity.ok(Map.of(
                "compteId", account.getId(),
                "solde", account.getSolde(),
                "currency", account.getCurrency()
        ));
    }
}
