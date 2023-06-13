package com.dh.transactionservice.service;

import com.dh.transactionservice.entities.Transaction;
import com.dh.transactionservice.exceptions.BadRequestException;
import com.dh.transactionservice.exceptions.ResourceNotFountException;
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
    public Transaction createTransaction (Transaction transaction) throws BadRequestException {
        if (!isValidCvuLength(transaction.getDestination_cvu())){
            throw new BadRequestException("We cannot create the transaction because the length of the destination_cvu is not correct");
        } else if (!validNumericCvu(transaction.getDestination_cvu())) {
           throw new BadRequestException("Invalid CVU format. The destination_cvu must contain only numeric characters.");
        }
        if (!isValidCvuLength(transaction.getOrigin_cvu())){
            throw new BadRequestException("We cannot create the user account because the length of the origin_cvu is not correct");
        } else if (!validNumericCvu(transaction.getOrigin_cvu())) {
            throw new BadRequestException("Invalid CVU format. The origin_cvu must contain only numeric characters.");
        }

        return repository.save(transaction);
    }
    private boolean isValidCvuLength(String cvu){
        return cvu.length() == 22;
    }
    private boolean validNumericCvu(String cvu){
        return cvu.matches("\\d+");
    }
}
