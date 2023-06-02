package com.dh.cardsservice.service;

import com.dh.cardsservice.entities.Card;
import com.dh.cardsservice.repository.ICardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class CardService {

    @Autowired
    ICardRepository cardRepository;

    public Card saveCard(Card card){
        return cardRepository.save(card);
    }
    public Optional<Card> findByCardNumber(String cardNumber){
        return Optional.ofNullable(cardRepository.findByCardNumber(cardNumber));
    }
    public Optional<List<Card>> findAllCardsByAccountId(Long id){
        return cardRepository.findAllCardsByAccountId(id);
    }
    public Optional<Card> findCardById(Long id) {
        return cardRepository.findById(id);
    }
}
