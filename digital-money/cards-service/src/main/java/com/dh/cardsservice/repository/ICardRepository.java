package com.dh.cardsservice.repository;

import com.dh.cardsservice.entities.Card;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICardRepository {

    Optional<List<Card>> findAllCardsByAccountId(Long accountId);
}
