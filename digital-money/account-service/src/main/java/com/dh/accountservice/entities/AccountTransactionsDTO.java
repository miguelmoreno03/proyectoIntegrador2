package com.dh.accountservice.entities;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

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
