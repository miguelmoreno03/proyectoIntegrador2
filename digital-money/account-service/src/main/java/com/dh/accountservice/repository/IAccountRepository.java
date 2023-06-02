package com.dh.accountservice.repository;

import com.dh.accountservice.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IAccountRepository extends JpaRepository <Account,Long> {
 Optional<Account> findByAlias(String alias);
 @Query("SELECT a FROM Account a WHERE a.user_id=:userId")
 Optional<Account> findByUserId (@Param("userId")Long userId);

 Optional<Account> findByCvu(String cvu);
}
