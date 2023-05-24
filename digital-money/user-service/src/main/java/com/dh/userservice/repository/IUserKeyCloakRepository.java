package com.dh.userservice.repository;

import com.dh.userservice.entities.AppUser;
import org.springframework.stereotype.Repository;


public interface IUserKeyCloakRepository {
    public AppUser createUser (AppUser user);
}
