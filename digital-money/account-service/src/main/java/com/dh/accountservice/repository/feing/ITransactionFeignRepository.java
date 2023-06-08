package com.dh.accountservice.repository.feing;


import com.dh.accountservice.entities.Transaction;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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



}
