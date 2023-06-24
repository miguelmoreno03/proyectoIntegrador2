package com.dh.transactionservice.controller;

import com.dh.transactionservice.entities.Transaction;
import com.dh.transactionservice.exceptions.BadRequestException;
import com.dh.transactionservice.exceptions.ResourceNotFountException;
import com.dh.transactionservice.service.TransactionService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@OpenAPIDefinition(info = @Info(title = "Transaction-Service-API",version = "1.0.0",description = "API to manage the transactions"))
@RestController
@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    TransactionService transactionService;
    @Operation(summary = "Get last 5 Transaction by his account id ",description = "Obtain the information of an existing Transactions from the database,the endpoint return the last 5 transactions," +
            " if it does not find it returns a not found")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Transaction.class)))),
            @ApiResponse(responseCode = "404",description = "We did not find any transactions related to the account ID.",content = @Content(schema = @Schema(implementation = Void.class))),
    })
    @GetMapping("/{id}")
    public ResponseEntity<List<Transaction>> searchLatestTransactionsByAccountId (@PathVariable Long id ) throws ResourceNotFountException {
        Optional<List<Transaction>> transactionsSearched = transactionService.listLastTransactionsByAccountId(id);
        return transactionsSearched.map(ResponseEntity::ok).orElseGet(()-> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
    @Operation(summary = "Get all Transactions by his account id",description = "Obtain the information of an existing Transactions from the database,the endpoint return all activities," +
            " if it does not find it returns a not found")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Transaction.class)))),
            @ApiResponse(responseCode = "404",description = "We did not find any transactions related to the account ID.",content = @Content(schema = @Schema(implementation = Void.class))),
    })
    @GetMapping("/all/{id}")
    public ResponseEntity<List<Transaction>> searchTransactionsByAccountId (@PathVariable Long id ) throws ResourceNotFountException {
        Optional<List<Transaction>> transactionsSearched = transactionService.transactionsByAccountId(id);
        return transactionsSearched.map(ResponseEntity::ok).orElseGet(()-> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
    @Operation(summary = "Get all Transactions by his account id and a range of the amount ",description = "Obtain the information of an existing Transactions from the database,the endpoint return all activities," +
            " if it does not find it returns a not found")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Transaction.class)))),
            @ApiResponse(responseCode = "404",description = "We did not find any transactions related to the account ID.",content = @Content(schema = @Schema(implementation = Void.class))),
    })
    @GetMapping("/all/{accountId}/{rangeA}/{rangeB}")
    public ResponseEntity<List<Transaction>> searchTransactionsByAccountIdAndRange (@PathVariable Long accountId, @PathVariable Double rangeA,@PathVariable Double rangeB ) throws ResourceNotFountException {
        Optional<List<Transaction>> transactionsSearched = transactionService.transactionsByAccountIdAndRange(accountId,rangeA,rangeB);
        return transactionsSearched.map(ResponseEntity::ok).orElseGet(()-> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
    @Operation(summary = "Create a transaction ",description = "We create a transaction with the required body, create the transaction only if the created body meets all the requirements")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",content = @Content(schema = @Schema(implementation = Transaction.class))),
            @ApiResponse(responseCode = "400",description = "<ul><li>We cannot create the transaction because the length of the destination_cvu is not correct. </li> " +
                    "<li>Invalid CVU format. The destination_cvu must contain only numeric characters.</li>"+
                    "<li>We cannot create the user account because the length of the origin_cvu is not correct.</li>"+
                    "<li>Invalid CVU format. The origin_cvu must contain only numeric characters.</li>",content = @Content(schema = @Schema(implementation = Void.class)))
    })
    @PostMapping
    public ResponseEntity <Transaction> createTransaction(@RequestBody Transaction transaction) throws BadRequestException {
        return ResponseEntity.ok(transactionService.createTransaction(transaction));
    }

}
