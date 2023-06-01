package com.dh.accountservice.entities;

import java.time.LocalDateTime;

public class Card {
    private Long id;

    private String type;

    private Double balance;

    private Long account_id;

    private String card_number;

    private String account_holder;

    private LocalDateTime expire_Date;

    private String bank_entity;
}
