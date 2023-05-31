package com.dh.userservice.repository;

import com.dh.userservice.entities.AppUser;
import com.dh.userservice.entities.AppUserDTO;


public interface IUserKeyCloakRepository {
     void createUser (AppUser user);
    void patchUser (String username , AppUserDTO user);

}
