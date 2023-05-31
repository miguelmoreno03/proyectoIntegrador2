package com.dh.accountservice.controller;

import com.dh.accountservice.entities.Account;
import com.dh.accountservice.entities.AccountCreateRequestDTO;
import com.dh.accountservice.entities.AccountDTO;
import com.dh.accountservice.entities.AccountTransactionsDTO;
import com.dh.accountservice.exceptions.BadRequestException;
import com.dh.accountservice.exceptions.ResourceNotFountException;
import com.dh.accountservice.service.AccountService;
import feign.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    @Autowired
    AccountService accountService;
    @GetMapping("/{id}/transactions")
   public ResponseEntity<AccountTransactionsDTO>  findTransactionsByAccountId(@PathVariable Long id ){
        return ResponseEntity.ok( accountService.findLastTransactionsByAccountId(id));
    }
    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> findAccountById (@PathVariable Long id) throws ResourceNotFountException {
        try {
            AccountDTO accountDTO = accountService.findAccountById(id);
            return ResponseEntity.ok(accountDTO);
        } catch (ResourceNotFountException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @GetMapping("/user/{id}")
    public ResponseEntity<AccountDTO> findAccountByUserId (@PathVariable Long id) throws ResourceNotFountException {
        try {
            AccountDTO accountDTO = accountService.findAccountByUserId(id);
            return ResponseEntity.ok(accountDTO);
        } catch (ResourceNotFountException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

   @PostMapping
    public ResponseEntity<AccountDTO> createAccount (@RequestBody AccountCreateRequestDTO account) throws IOException, BadRequestException {
        return ResponseEntity.ok(accountService.createAccount(account));
   }
   @PatchMapping("/{id}")
    public ResponseEntity<AccountDTO>  patchAccount (@PathVariable Long id)  throws IOException, BadRequestException{
        return ResponseEntity.ok(accountService.patchAccount(id));
   }


}
