package com.dh.accountservice.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateTransactionDTO {

    @NotNull
    @Size(min = 1, max = 250)
    private LocalDateTime date;
    @NotNull
    @Size(min = 1, max = 250)
    private String description;
    @Column
    @NotNull
    private Double amount;
    @NotNull
    @Size(min = 22, max = 22)
    @Schema(description = "destination cvu has to be numeric and has to have 22 characters")
    private String destination_cvu;
    @NotNull
    @Size(min = 1, max = 250)
    private String type;
}
