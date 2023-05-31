package com.dh.userservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    @JsonIgnore
    private Long id;

    private String alias;

    private String cvu;
    @JsonIgnore
    private Double balance;

   @JsonIgnore
    private Long user_id;
}
