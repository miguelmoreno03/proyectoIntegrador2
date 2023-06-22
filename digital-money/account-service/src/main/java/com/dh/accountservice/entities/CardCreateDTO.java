package com.dh.accountservice.entities;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CardCreateDTO {
    @Size(min = 1, max = 250)
    @NotNull
    private String type;
    @NotNull
    private Double balance;
    @Size(min = 13, max = 18)
    @NotNull
    private String cardNumber;
    @Size(min = 1, max = 250)
    @NotNull
    private String accountHolder;
    @NotNull
    private LocalDateTime expireDate;
    @Size(min = 1, max = 250)
    @NotNull
    private String bankEntity;
}
