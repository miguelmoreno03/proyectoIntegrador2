package com.dh.transactionservice.controller;

import com.dh.transactionservice.entities.Transaction;
import com.dh.transactionservice.exceptions.BadRequestException;
import com.dh.transactionservice.exceptions.ResourceNotFountException;
import com.dh.transactionservice.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    TransactionService transactionService;

    @GetMapping("/{id}")
    public ResponseEntity<List<Transaction>> searchLatestTransactionsByAccountId (@PathVariable Long id )  {
        Optional<List<Transaction>> transactionsSearched = transactionService.listLastTransactionsByAccountId(id);
        return transactionsSearched.map(ResponseEntity::ok).orElseGet(()-> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
    @GetMapping("/all/{id}")
    public ResponseEntity<List<Transaction>> searchTransactionsByAccountId (@PathVariable Long id )  {
        Optional<List<Transaction>> transactionsSearched = transactionService.transactionsByAccountId(id);
        return transactionsSearched.map(ResponseEntity::ok).orElseGet(()-> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
    @GetMapping("/all/{accountId}/{rangeA}/{rangeB}")
    public ResponseEntity<List<Transaction>> searchTransactionsByAccountIdAndRange (@PathVariable Long accountId, @PathVariable Double rangeA,@PathVariable Double rangeB )  {
        Optional<List<Transaction>> transactionsSearched = transactionService.transactionsByAccountIdAndRange(accountId,rangeA,rangeB);
        return transactionsSearched.map(ResponseEntity::ok).orElseGet(()-> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
    @PostMapping
    public ResponseEntity <Transaction> createTransaction(@RequestBody Transaction transaction) throws BadRequestException {
        return ResponseEntity.ok(transactionService.createTransaction(transaction));
    }
}
