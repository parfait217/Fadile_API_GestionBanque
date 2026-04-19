package com.example.demo.service;

import com.example.demo.Entity.Account;
import com.example.demo.Entity.Transaction;
import com.example.demo.exception.NotfoundException;
import com.example.demo.exception.UnauthorizeException;
import com.example.demo.repository.AccountRepo;
import com.example.demo.repository.TransactionRepo;
import com.example.demo.service.Iservice.ITransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService implements ITransactionService {

    private final AccountRepo accountRepo;
    private final TransactionRepo transactionRepo;

    @Override
    @Transactional
    public Transaction depot(Long compteId, BigDecimal montant, String description) {
        Account compte = accountRepo.findById(compteId)
                .orElseThrow(() -> new NotfoundException("Compte introuvable"));

        compte.setSolde(compte.getSolde().add(montant));
        accountRepo.save(compte);

        Transaction tx = new Transaction("DEPOT", montant, compte, description);
        return transactionRepo.save(tx);
    }

    @Override
    @Transactional
    public Transaction retrait(Long compteId, BigDecimal montant, String description) {
        Account compte = accountRepo.findById(compteId)
                .orElseThrow(() -> new NotfoundException("Compte introuvable"));

        if (compte.getSolde().compareTo(montant) < 0) {
            throw new UnauthorizeException("Solde insuffisant");
        }

        compte.setSolde(compte.getSolde().subtract(montant));
        accountRepo.save(compte);

        Transaction tx = new Transaction("RETRAIT", montant, compte, description);
        return transactionRepo.save(tx);
    }

    @Override
    @Transactional
    public Transaction virement(Long compteSourceId, Long compteDestinataireId, BigDecimal montant, String description) {
        Account compteSource = accountRepo.findById(compteSourceId)
                .orElseThrow(() -> new NotfoundException("Compte source introuvable"));
        Account compteDestinataire = accountRepo.findById(compteDestinataireId)
                .orElseThrow(() -> new NotfoundException("Compte destinataire introuvable"));

        if (compteSource.getSolde().compareTo(montant) < 0) {
            throw new UnauthorizeException("Solde insuffisant pour le virement");
        }

        compteSource.setSolde(compteSource.getSolde().subtract(montant));
        compteDestinataire.setSolde(compteDestinataire.getSolde().add(montant));

        accountRepo.save(compteSource);
        accountRepo.save(compteDestinataire);

        String desc = description != null ? description : "Virement vers " + compteDestinataire.getName();
        Transaction tx = new Transaction("VIREMENT", montant, compteSource, desc);
        return transactionRepo.save(tx);
    }

    @Override
    public List<Transaction> getReleve(Long compteId) {
        if (!accountRepo.existsById(compteId)) {
            throw new NotfoundException("Compte introuvable");
        }
        return transactionRepo.findByCompteIdOrderByDateDesc(compteId);
    }

    @Override
    public Account getSolde(Long compteId) {
        return accountRepo.findById(compteId)
                .orElseThrow(() -> new NotfoundException("Compte introuvable"));
    }
}