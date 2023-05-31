package com.dh.userservice.repository.feing;

import com.dh.userservice.entities.Account;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient(name = "accounts-service",url = "http://localhost:8084")
public interface IAccountFeignRepository {
    @GetMapping("/accounts/user/{userId}")
    Optional<Account> findAccountByUserId (@PathVariable Long userId);
}
