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
    public Optional<List<Transaction>> listLastTransactionsByAccountId(Long id){
       return repository.findLatestTransactionsByAccountId(id);
    }
    public Optional<List<Transaction>> transactionsByAccountId(Long id){
        return repository.findTransactionsByAccountId(id);
    }
    public Optional<List<Transaction>> transactionsByAccountIdAndRange(Long id,Double rangeA,Double rangeB ){
        return repository.findTransactionsByAccountIdAndAmountRange(id, rangeA, rangeB);
    }
}
