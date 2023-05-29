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


}
