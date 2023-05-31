package com.dh.cardsservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;
    private Double balance;

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "account_holder")
    private String accountHolder;

    @Column(name = "expire_date")
    private Date expireDate;

    @Column(name = "bank_entity")
    private String bankEntity;
}
