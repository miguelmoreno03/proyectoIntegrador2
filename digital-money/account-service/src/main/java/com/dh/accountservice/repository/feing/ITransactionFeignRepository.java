package com.dh.accountservice.repository.feing;


import com.dh.accountservice.entities.Transaction;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@FeignClient (name = "transactions-service",url = "http://localhost:8083")
public interface ITransactionFeignRepository {
    @GetMapping("/transactions/{accountId}" )
    Optional<List<Transaction>> findAllByAccountId(@PathVariable Long accountId);

    @GetMapping("/transactions/all/{accountId}")
    Optional<List<Transaction>> findTransactionsByAccountId(@PathVariable Long accountId);
    @GetMapping("/transactions/all/{accountId}/{rangeA}/{rangeB}")
    Optional<List<Transaction>> findTransactionsByAccountIdAndRange(@PathVariable Long accountId, @PathVariable Double rangeA,@PathVariable Double rangeB);
    @PostMapping ("/transactions")
    Transaction createTransaction(@RequestBody Transaction transaction);
    @GetMapping("/transactions/{transactionId}/transference")
    byte[]generateReceipt(@PathVariable Long transactionId);
    @GetMapping("/transactions/one/{id}")
   Optional<Transaction>  findTransactionById (@PathVariable Long id);



}
