package com.dh.userservice.repository;

import com.dh.userservice.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository  <AppUser,Integer>{
    AppUser findOneByEmail (String email);

}
