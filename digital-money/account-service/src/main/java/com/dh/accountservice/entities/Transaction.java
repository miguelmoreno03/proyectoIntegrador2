package com.dh.accountservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    private Long id;

    private Long account_id;

    private LocalDateTime date;

    private String description;

    private Double amount;

    private String destination_cvu;

    private String origin_cvu;

    private String type;
}
