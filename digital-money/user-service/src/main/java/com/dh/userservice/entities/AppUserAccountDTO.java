package com.dh.userservice.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class AppUserAccountDTO {

    private Long id;

    private String first_name ;

    private String last_name ;

    private String dni ;

    private String email ;

    private String phone ;

    private Account account;
}
