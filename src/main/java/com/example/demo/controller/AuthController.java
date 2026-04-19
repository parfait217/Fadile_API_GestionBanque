package com.example.demo.controller;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.ClientDTO;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.repository.ClientRepo;
import com.example.demo.service.Iservice.IClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "6.1 Authentification", description = "Endpoints publics - Pas besoin de token")
public class AuthController {

    private final IClientService clientService;
    private final ClientRepo clientRepo;

    @PostMapping("/register")
    @Operation(summary = "Inscription", description = "Créer un nouveau compte client. Remplissez vos informations (nom, prénom, email, mot de passe, téléphone) pour vous inscrire.")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        if (clientRepo.existsByEmail(request.email())) {
            return ResponseEntity.badRequest()
                    .body(new AuthResponse(null, "Bearer", request.email(), null, "Email déjà utilisé"));
        }

        ClientDTO dto = new ClientDTO(null, request.nom(), request.prenom(), request.email(), request.telephone());
        ClientDTO created = clientService.createClient(dto, request.motDePasse());

        String token = "jwt-token-" + created.id();
        return new ResponseEntity<>(
                new AuthResponse(token, "Bearer", created.email(), created.id(), "Inscription réussie"),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/login")
    @Operation(summary = "Connexion", description = "Se connecter avec son email et mot de passe. Retourne un token JWT à utiliser pour les autres requêtes.")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        boolean authenticated = clientService.authenticate(request.email(), request.motDePasse());

        if (!authenticated) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse(null, "Bearer", request.email(), null, "Email ou mot de passe incorrect"));
        }

        var client = clientRepo.findByEmail(request.email()).orElse(null);
        String token = "jwt-token-" + client.getId();

        return ResponseEntity.ok(new AuthResponse(token, "Bearer", client.getEmail(), client.getId(), "Connexion réussie"));
    }

    @GetMapping("/test")
    @Operation(summary = "Test API", description = "Vérifie que l'API est opérationnelle. Simple ping pour s'assurer que le serveur fonctionne.")
    public ResponseEntity<Map<String, Object>> test() {
        return ResponseEntity.ok(Map.of(
                "status", "OK",
                "message", "API opérationnelle",
                "timestamp", System.currentTimeMillis()
        ));
    }
}