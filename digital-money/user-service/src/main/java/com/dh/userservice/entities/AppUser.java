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
    private String first_name ;
    @Column
    private String last_name ;
    @Column
    private String dni ;
    @Column
    private String email ;
    @Column
    private String phone ;
    @Column
    private String password;
    @Column
    private String cvu;
    @Column
    private String alias;

    public AppUser() {
    }
}
