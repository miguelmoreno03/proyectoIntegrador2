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


    @PostMapping()
    public ResponseEntity<Card> saveCard(@RequestBody Card card) {
        Optional<Card> existingCard = cardService.findByCardNumber(card.getCardNumber());

        if (existingCard.isPresent() && !existingCard.get().getAccountId().equals(card.getAccountId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Card savedCard = cardService.saveCard(card);

        if (savedCard != null) {
            return ResponseEntity.status(HttpStatus.OK).body(savedCard);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

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

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id){
        if (cardService.deleteCardById(id)){
            return ResponseEntity.ok("Se eliminó la tarjeta con id " + id);
        }else {
            return  ResponseEntity.notFound().build();
        }
    }
}