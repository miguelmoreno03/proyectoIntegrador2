package com.dh.accountservice.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String alias;
    @Column
    private String cvu;
    @Column
    private Double balance;
    @Column
    private Long user_id;
    @Transient
    private List<Transaction> transactions;
    @Transient
    private Set<Card> cards;
}
