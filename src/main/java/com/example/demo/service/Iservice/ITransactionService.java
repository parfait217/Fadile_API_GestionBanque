package com.example.demo.service.Iservice;

import com.example.demo.Entity.Account;
import com.example.demo.Entity.Transaction;

import java.math.BigDecimal;
import java.util.List;

public interface ITransactionService {
    Transaction depot(Long compteId, BigDecimal montant, String description);
    Transaction retrait(Long compteId, BigDecimal montant, String description);
    Transaction virement(Long compteSource, Long compteDestinataire, BigDecimal montant, String description);
    List<Transaction> getReleve(Long compteId);
    Account getSolde(Long compteId);
}