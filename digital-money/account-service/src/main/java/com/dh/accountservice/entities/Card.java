package com.dh.accountservice.entities;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

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
