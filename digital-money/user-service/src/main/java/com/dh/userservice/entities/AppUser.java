package com.dh.userservice.entities;



import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")

public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name ;
    @Column
    private String last_name ;
    @Column
    private Long dni ;
    @Column
    private String email ;
    @Column
    private Long phone ;
    @Column
    private String password;

    @Column
    private Long cvu ;
    @Column
    private String alias;




}
