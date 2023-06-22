package com.dh.accountservice.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountCardsDTO {
    @Schema(description = "id of my userAccount, usually generated by the database")
    private Long id;
    @Schema(description = "alias of my user account, it is generated on a txt taking random words")
    @Size(min = 1, max = 250)
    private String alias;
    @NotNull
    @Size(min = 22, max = 22)
    private String cvu;
    @NotNull
    private Double balance;

    @JsonIgnoreProperties(value = {"accountId"})
    private List<Card> cards;
}
