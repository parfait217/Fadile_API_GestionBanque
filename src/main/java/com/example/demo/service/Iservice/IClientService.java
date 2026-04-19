package com.example.demo.service.Iservice;

import com.example.demo.dto.ClientDTO;

import java.util.List;

public interface IClientService {
    ClientDTO createClient(ClientDTO dto, String motDePasse);
    List<ClientDTO> getAllClients();
    ClientDTO getClientById(Long id);
    void deleteClient(Long id);
    boolean authenticate(String email, String motDePasse);
}