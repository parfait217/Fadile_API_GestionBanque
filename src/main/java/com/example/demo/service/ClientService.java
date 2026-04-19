package com.example.demo.service;

import com.example.demo.Entity.Client;
import com.example.demo.dto.ClientDTO;
import com.example.demo.exception.NotfoundException;
import com.example.demo.repository.ClientRepo;
import com.example.demo.service.Iservice.IClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientService implements IClientService {

    private final ClientRepo clientRepo;

    @Override
    public ClientDTO createClient(ClientDTO dto, String motDePasse) {
        Client client = new Client(
                dto.nom(),
                dto.prenom(),
                dto.email(),
                motDePasse,
                dto.telephone()
        );
        Client saved = clientRepo.save(client);
        return toDTO(saved);
    }

    @Override
    public List<ClientDTO> getAllClients() {
        return clientRepo.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ClientDTO getClientById(Long id) {
        Client client = clientRepo.findById(id)
                .orElseThrow(() -> new NotfoundException("Client introuvable avec l'id : " + id));
        return toDTO(client);
    }

    @Override
    public void deleteClient(Long id) {
        if (!clientRepo.existsById(id)) {
            throw new NotfoundException("Client introuvable avec l'id : " + id);
        }
        clientRepo.deleteById(id);
    }

    @Override
    public boolean authenticate(String email, String motDePasse) {
        return clientRepo.findByEmail(email)
                .map(c -> c.getMotDePasse().equals(motDePasse))
                .orElse(false);
    }

    private ClientDTO toDTO(Client client) {
        return new ClientDTO(
                client.getId(),
                client.getNom(),
                client.getPrenom(),
                client.getEmail(),
                client.getTelephone()
        );
    }
}