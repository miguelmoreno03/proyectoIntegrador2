package com.dh.userservice.repository;

import com.dh.userservice.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository  <AppUser,Integer>{
    Optional<AppUser> findOneByEmail (String email);

}
