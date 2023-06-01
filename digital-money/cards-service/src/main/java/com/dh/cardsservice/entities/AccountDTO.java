package com.dh.cardsservice.entities;

import java.util.List;

public class AccountDTO {
    private Long id;
    private String alias;
    private String cvu;
    private Double balance;
    private UserDTO user;
    private List<TransactionDTO> transactions;
    private List<Card> cards;
}
