package com.dh.cardsservice.controller;

import com.dh.cardsservice.entities.Card;
import com.dh.cardsservice.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/cards")
@RequiredArgsConstructor
public class CardController {

    @Autowired
    CardService cardService;

    @GetMapping("accounts/{id}")
    public ResponseEntity<List<Card>> searchCardsByAccountId (@PathVariable Long id )  {
        Optional<List<Card>> cardsSearched = cardService.findAllCardsByAccountId(id);
        return cardsSearched.map(ResponseEntity::ok).orElseGet(()-> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Card> findCardById(@PathVariable Long id) {
        Optional<Card> cardSearched = cardService.findCardById(id);
        return cardSearched.map(ResponseEntity::ok).orElseGet(()-> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}