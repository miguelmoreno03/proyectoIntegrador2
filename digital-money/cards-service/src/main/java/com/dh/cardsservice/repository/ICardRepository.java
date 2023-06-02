package com.dh.cardsservice.repository;

import com.dh.cardsservice.entities.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICardRepository extends JpaRepository<Card,Long> {

    Card findByCardNumber(String cardNumber);
    Optional<List<Card>> findAllCardsByAccountId(Long accountId);
}
