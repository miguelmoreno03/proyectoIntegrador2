package com.dh.accountservice.controller;

import com.dh.accountservice.entities.*;
import com.dh.accountservice.exceptions.BadRequestException;
import com.dh.accountservice.exceptions.ConflictException;
import com.dh.accountservice.exceptions.ResourceNotFountException;
import com.dh.accountservice.service.AccountService;
import feign.Response;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@OpenAPIDefinition(info = @Info(title = "Account-Service-API",version = "1.0.0",description = "API to manage the Accounts"))
@RestController
@RequestMapping("/accounts")

public class AccountController {
    @Autowired
    AccountService accountService;
    @Operation(summary = "Adding a card from account ",description = "Create a new card from the account service ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = AccountTransactionsDTO.class))),
            @ApiResponse(responseCode = "400",description = "<ul> <li>you are trying to create a card and associate it from a non-existent account.</li>"  +
                    "<li>We can´t create the card, the card number must be only numeric characters.</li> " +
                    "<li>We don't found any userAccount with the id:  + userAccountId. </li></ul>" ,content = @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "409",description = "<ul> <li> We got a problem the card number it´s already associated with the user account</li>" +
                    "<li></li> </ul>")
    })
    @PostMapping("/{id}/cards")
    public ResponseEntity<Card> saveCardForAccount(@PathVariable Long id, @RequestBody CardCreateDTO cardCreateDTO) throws IOException, BadRequestException, ConflictException {
        return accountService.saveCardForAccount(id, cardCreateDTO);
    }
    @Operation(summary = "Get an Account  with the last 5 transactions information ",description = "Obtain the information of an existing Account with his last transactions from the database, if it does not find it, it returns a not found")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = AccountTransactionsDTO.class))),
            @ApiResponse(responseCode = "404",description = "<ul> <li>We don't found any Transactions associated  with this userAccount id:  +userAccountId .</li>"  +
                    "<li>We have problems  with the transactions-service try later.</li> " +
                    "<li>We don't found any userAccount with the id:  + userAccountId. </li></ul>",content = @Content(schema = @Schema(implementation = Void.class))),
    })
    @GetMapping("/{id}/transactions")
    public ResponseEntity<AccountTransactionsDTO>  findTransactionsByAccountId(@PathVariable Long id ) throws ResourceNotFountException {
        return ResponseEntity.ok(accountService.findLastTransactionsByAccountId(id));
    }
    @Operation(summary = "Get an Account  with all transactions information can take a range to filter this transactions",description = "Obtain the information of an existing Account with his transactions from the database, if it does not find it, it returns a not found")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = AccountTransactionsDTO.class))),
            @ApiResponse(responseCode = "404",description = "<ul> <li> We don't found any Transactions associated  with this userAccount id: +userAccountId and with the specific range. </li>" +
                    "<li>We don't found any Transactions associated  with this userAccount id:  +userAccountId .</li>"  +
                    "<li>We have problems  with the transactions-service try later.</li> " +
                    "<li>We don't found any userAccount with the id:  + userAccountId. </li></ul>",content = @Content(schema = @Schema(implementation = Void.class))),
    })
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
    @Operation(summary = "Get an Account  with all cards information ",description = "Obtain the information of an existing Account with his cards from the database, if it does not find it, it returns a not found")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = AccountsCardDTO.class))),
            @ApiResponse(responseCode = "404",description = "<ul> <li> We don't found any Cards associated  with this userAccount id:  +accountId. </li>"  +
                    "<li>We have problems  with the cards-service try later.</li> " +
                    "<li>We don't found any userAccount with the id: userAccountId. </li></ul>",content = @Content(schema = @Schema(implementation = Void.class))),
    })
    @GetMapping("/{id}/cards")
    public ResponseEntity<AccountCardsDTO>  findAllCardsByAccountId(@PathVariable Long id ) throws ResourceNotFountException {
            return ResponseEntity.ok(accountService.findAllCardsByAccountId(id));
    }
    @Operation(summary = "Get an Account  by his Id with a specific card information ",description = "Obtain the information of an existing Account with the a card from the database, if it does not find it, it returns a not found")
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
    @Operation(summary = "Get an Account  by his Id ",description = "Obtain the information of an existing Account from the database, if it does not find it, it returns a not found")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = AccountDTO.class))),
            @ApiResponse(responseCode = "404",description = "We don´t found any account associated with the id:  + accountId",content = @Content(schema = @Schema(implementation = Void.class))),
    })
    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> findAccountById (@PathVariable Long id) throws ResourceNotFountException {
            return ResponseEntity.ok(accountService.findAccountById(id));
    }
    @Operation(summary = "Get an Account  by his User Id ",description = "Obtain the information of an existing Account from the database, if it does not find it, it returns a not found")
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

    @Operation(summary = "Create a transaction ",description = "We create a transaction with the required body, create the transaction only if the created body meets all the requirements")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",content = @Content(schema = @Schema(implementation = Transaction.class))),
            @ApiResponse(responseCode = "400",description = "<ul><li>We cannot create the transaction because the length of the destination_cvu is not correct. </li> " +
                    "<li>Invalid CVU format. The destination_cvu must contain only numeric characters.</li>"+
                    "<li>We cannot create the user account because the length of the origin_cvu is not correct.</li>"+
                    "<li>Invalid CVU format. The origin_cvu must contain only numeric characters.</li>" +
                    "<li>You´re trying to create a transaction from a  non-existent account. </li>" +
                    "<li>You do not have the necessary money to make the transaction try to add funds and try again later.</li>" +
                    "<li>We have problems with the transaction-service try later.</li>",content = @Content(schema = @Schema(implementation = Void.class)))
    })

   @PostMapping("/{id}/transferences")
   public ResponseEntity<Transaction> createTransaction (@RequestBody CreateTransactionDTO transactionDTO,@PathVariable Long id) throws BadRequestException {

        return ResponseEntity.ok(accountService.createTransaction(transactionDTO,id));
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
    @Operation(summary = "Generate a receipt from a transaction ",description = "We create a receipt from a transaction")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",content = @Content(schema = @Schema(implementation = byte[].class))),
            @ApiResponse(responseCode = "400",description = "<ul><li>Error creating PDF document: +  exception.getMessage(). </li> " +
                    "<li>Error accessing transaction properties: + exception.getMessage().</li>"+
                    "<li>Error in the input or output of the document + exception.getMessage()</li>"+
                    "<li>You´re trying to generate a receipt of a non-existence transaction.</li>" +
                    "<li>you are trying to generate a receipt for a transaction that is not from your account.</li>" +
                    "<li>You´re trying to get a receipt from a transaction from a non-existence account.</li></ul>",content = @Content(schema = @Schema(implementation = Void.class)))
    })
   @GetMapping("/{accountId}/transferences/{transactionId}/comprobante")
    public ResponseEntity<byte[]> generateReceipt (@PathVariable Long accountId,@PathVariable Long transactionId) throws  BadRequestException {
       byte[] pdfBytes = accountService.generateReceipt(transactionId,accountId);
       HttpHeaders headers = new HttpHeaders();
       headers.setContentType(MediaType.APPLICATION_PDF);
       headers.setContentDispositionFormData("attachment", "transaction.pdf");

       return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
   }



}
