package com.dh.accountservice.controller;

import com.dh.accountservice.entities.*;
import com.dh.accountservice.exceptions.BadRequestException;
import com.dh.accountservice.exceptions.ResourceNotFountException;
import com.dh.accountservice.service.AccountService;
import feign.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("/accounts")

public class AccountController {
    @Autowired
    AccountService accountService;

    @PostMapping("/{id}/cards")
    public ResponseEntity<Card> saveCardForAccount(@PathVariable Long id, @RequestBody CardCreateDTO cardCreateDTO) throws IOException, BadRequestException {
        return accountService.saveCardForAccount(id, cardCreateDTO);
    }

    @GetMapping("/{id}/transactions")
    public ResponseEntity<AccountTransactionsDTO>  findTransactionsByAccountId(@PathVariable Long id ) throws ResourceNotFountException {
        return ResponseEntity.ok(accountService.findLastTransactionsByAccountId(id));
    }

    @GetMapping("/{id}/activity")
    public ResponseEntity<AccountTransactionsDTO>  findAllTransactionsByAccountId(@PathVariable Long id ,
                                                                                  @RequestParam(required = false) Double rangeA,
                                                                                  @RequestParam(required = false) Double rangeB)
                                                                                  throws ResourceNotFountException {
            AccountTransactionsDTO transactionsDTO;

            if (rangeA != null && rangeB != null) {
                transactionsDTO = accountService.findTransactionsByAccountIdAndRange(id, rangeA, rangeB);
            } else {
                transactionsDTO = accountService.findTransactionsByAccountId(id);
            }

            return ResponseEntity.ok(transactionsDTO);
    }

    @GetMapping("/{id}/cards")
    public ResponseEntity<AccountCardsDTO>  findAllCardsByAccountId(@PathVariable Long id ) throws ResourceNotFountException {
            return ResponseEntity.ok(accountService.findAllCardsByAccountId(id));
    }
    @Operation(summary = "Get a Account  by his Id with a specific card information ",description = "Obtain the information of an existing Account with the a card from the database, if it does not find it, it returns a not found")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = AccountsCardDTO.class))),
            @ApiResponse(responseCode = "404",description = "<ul> <li> We don't found any Card associated  with this userAccount id: + accountId. </li>"  +
                    "<li>We don't found any userAccount with the id: + accountId. </li> " +
                    "<li>We have problems  with the cards-service try later. </li></ul>",content = @Content(schema = @Schema(implementation = Void.class))),
    })
    @GetMapping("/{accountId}/cards/{cardId}")
    public ResponseEntity<AccountsCardDTO>  findAccountWithCard(@PathVariable Long accountId, @PathVariable Long cardId) throws ResourceNotFountException {
            return ResponseEntity.ok(accountService.findAccountWithCardById(accountId, cardId));

    }
    @Operation(summary = "Get a Account  by his Id ",description = "Obtain the information of an existing Account from the database, if it does not find it, it returns a not found")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = AccountDTO.class))),
            @ApiResponse(responseCode = "404",description = "We don´t found any account associated with the id:  + accountId",content = @Content(schema = @Schema(implementation = Void.class))),
    })
    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> findAccountById (@PathVariable Long id) throws ResourceNotFountException {
            return ResponseEntity.ok(accountService.findAccountById(id));
    }
    @Operation(summary = "Get a Account  by his User Id ",description = "Obtain the information of an existing Account from the database, if it does not find it, it returns a not found")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = AccountDTO.class))),
            @ApiResponse(responseCode = "404",description = "we don´t found any account with the user_id : + userAccount",content = @Content(schema = @Schema(implementation = Void.class))),
    })
    @GetMapping("/user/{id}")
    public ResponseEntity<AccountDTO> findAccountByUserId (@PathVariable Long id) throws ResourceNotFountException {
            return ResponseEntity.ok(accountService.findAccountByUserId(id));
    }
    @Operation(summary = "Create an Account ",description = "create an Account  in my database from the information provided in the body")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = AccountDTO.class))),
            @ApiResponse(responseCode = "400",description = "<ul> <li>We cannot create the user account because the length of the cvu is not correct. </li>" +
                    "<li>Invalid CVU format. The CVU must contain only numeric characters. </li>"+
                    "<li>We can´t create the user account because the alias it´s already in use.</li>" +
                    "<li>We can´t create the user account because the user associated in the account its already in use. </li>" +
                    "<li>We can´t create the user account because the cvu associated in the account its already in use.</li> </ul>",content = @Content(schema = @Schema(implementation = Void.class))),
    })

   @PostMapping
    public ResponseEntity<AccountDTO> createAccount (@RequestBody AccountCreateRequestDTO account) throws IOException, BadRequestException {
        return ResponseEntity.ok(accountService.createAccount(account));
   }
    @Operation(summary = "Patch an Account ",description = "Patch an Account from  my database with the parameters  provided in the body")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = AccountDTO.class))),
            @ApiResponse(responseCode = "400",description = "<ul> <li>We can´t update the user account because the alias it´s already in use. </li>" +
                    "<li>We can´t update the user account because the user don´t exist. </li> </ul>",content = @Content(schema = @Schema(implementation = Void.class))),
    })
   @PatchMapping("/{id}")
    public ResponseEntity<AccountDTO>  patchAccount (@PathVariable Long id)  throws IOException, BadRequestException{
        return ResponseEntity.ok(accountService.patchAccount(id));
   }
    @ExceptionHandler(ResourceNotFountException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFountException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }


}
