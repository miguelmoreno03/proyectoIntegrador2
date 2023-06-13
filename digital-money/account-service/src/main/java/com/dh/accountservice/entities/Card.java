package com.dh.accountservice.entities;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Card {

    private Long id;

    private String type;

    private Double balance;

    private Long accountId;

    private String cardNumber;

    private String accountHolder;

    private LocalDateTime expireDate;

    private String bankEntity;
}
