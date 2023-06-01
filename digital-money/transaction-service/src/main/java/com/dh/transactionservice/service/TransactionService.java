package com.dh.transactionservice.service;

import com.dh.transactionservice.entities.Transaction;
import com.dh.transactionservice.repository.ITransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {
    @Autowired
    ITransactionRepository repository;
    public Optional<List<Transaction>> listTransactionsByAccountId(Long id){
       return repository.findLatestTransactionsByAccountId(id);
    }

}
