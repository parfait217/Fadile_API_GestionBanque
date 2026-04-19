package com.example.demo.controller;

import com.example.demo.dto.ClientDTO;
import com.example.demo.service.Iservice.IClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
@Tag(name = "6.2 Clients", description = "Gestion des clients - Token requis")
public class ClientController {

    private final IClientService clientService;

    @GetMapping
    @Operation(summary = "Liste des clients", description = "Affiche la liste de tous les clients enregistrés dans la banque.")
    public ResponseEntity<List<ClientDTO>> getAllClients() {
        return ResponseEntity.ok(clientService.getAllClients());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Détails client", description = "Affiche les详细信息 d'un client spécifique avec son ID.")
    public ResponseEntity<ClientDTO> getClientById(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.getClientById(id));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer client", description = "Supprime définitivement un client et toutes ses données associées (comptes, transactions).")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }
}