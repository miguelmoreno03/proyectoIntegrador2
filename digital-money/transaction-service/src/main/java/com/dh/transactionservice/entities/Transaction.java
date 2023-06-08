package com.dh.transactionservice.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transactions")
@ToString
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
   @Column
    private Long account_id;
    @Column
    private LocalDateTime date;
    @Column
    private String description;
    @Column
    private Double amount;
    @Column
    private String destination_cvu;
    @Column
    private String origin_cvu;
    @Column
    private String type;

}
