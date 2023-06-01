package com.dh.accountservice.repository.feing;

import com.dh.accountservice.entities.Card;
import jakarta.ws.rs.Path;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@FeignClient(name = "cards-service", url = "http://localhost:8085")
public interface ICardFeignRepository {
    @GetMapping("/cards/accounts/{accountId}" )
    Optional<List<Card>> findAllByAccountId(@PathVariable Long accountId);

    @GetMapping("/cards/{cardId}")
    Optional<Card> findCardById(@PathVariable Long cardId);
}
