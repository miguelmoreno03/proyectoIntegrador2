package com.dh.accountservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountTransactionsDTO {

    private Long id;

    private String alias;

    private String cvu;

    private Double balance;

   @JsonIgnoreProperties(value = {"account_id"})
    private List<Transaction> transactions;

}
