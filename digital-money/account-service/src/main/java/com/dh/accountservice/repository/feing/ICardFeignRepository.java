package com.dh.accountservice.repository.feing;

import com.dh.accountservice.entities.Card;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@FeignClient(name = "cards-service", url = "http://localhost:8085")
public interface ICardFeignRepository {
    @PostMapping("/cards")
    ResponseEntity<Card> saveCard(@RequestBody Card card);
    @GetMapping("/cards/accounts/{accountId}" )
    Optional<List<Card>> findAllByAccountId(@PathVariable Long accountId);

    @GetMapping("/cards/{cardId}")
    Optional<Card> findCardById(@PathVariable Long cardId);
}
