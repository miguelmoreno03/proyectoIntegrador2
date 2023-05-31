package com.dh.cardsservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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
    @Column
    private String type;
    @Column
    private Double balance;
    @Column
    private AccountDTO account;
    @Column
    private String card_number;
    @Column
    private String account_holder;
    @Column
    private LocalDateTime expire_Date;
    @Column
    private String bank_entity;
}
