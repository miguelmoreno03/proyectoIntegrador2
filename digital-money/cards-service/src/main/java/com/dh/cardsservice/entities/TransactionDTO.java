package com.dh.cardsservice.entities;

import jakarta.persistence.Column;

import java.time.LocalDateTime;

public class TransactionDTO {
    private Long id;
    private AccountDTO account;
    private LocalDateTime date;
    private String description;
    private Double amount;
    private String destination_cvu;
    private String origin_cvu;
    private String type;
}
