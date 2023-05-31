package com.dh.transactionservice.controller;

import com.dh.transactionservice.entities.Transaction;
import com.dh.transactionservice.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    TransactionService transactionService;

    @GetMapping("/{id}")
    public ResponseEntity<List<Transaction>> searchTransactionsByAccountId (@PathVariable Long id )  {
        Optional<List<Transaction>> transactionsSearched = transactionService.listTransactionsByAccountId(id);
        return transactionsSearched.map(ResponseEntity::ok).orElseGet(()-> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
