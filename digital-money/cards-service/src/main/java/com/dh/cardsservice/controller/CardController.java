package com.dh.cardsservice.controller;

import com.dh.cardsservice.entities.Card;
import com.dh.cardsservice.exceptions.BadRequestException;
import com.dh.cardsservice.exceptions.ConflictException;
import com.dh.cardsservice.exceptions.ResourceNotFountException;
import com.dh.cardsservice.service.CardService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/cards")
@RequiredArgsConstructor
@OpenAPIDefinition(info = @Info(title = "Cards-Service-API",version = "1.0.0",description = "API to manage the cards"))
public class CardController {

    @Autowired
    CardService cardService;

    @Operation(summary = "Create a Card ",description = "We create a card with the required body, create the card only if the created body meets all the requirements")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",content = @Content(schema = @Schema(implementation = Card.class))),
            @ApiResponse(responseCode = "409",description = "<ul><li>We got a problem the card number it´s already associated with the user account. </li> " +
                    "<li>We got a problem the card number it´s already associated with other account.</li>" +
                    "<li>We got a problem the card number it´s already associated with other account.</li>",content = @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "400",description = "We can´t create the card, the card number must be only numeric characters ",content = @Content(schema = @Schema(implementation = Void.class)))
    })
    @PostMapping()
    public ResponseEntity<Card> saveCard(@RequestBody Card card) throws BadRequestException, ConflictException {
        return ResponseEntity.ok(cardService.saveCard(card));

    }
    @Operation(summary = "Get all Cards by his account id  ",description = "Obtain the information of an existing Cards from the database,the endpoint return all cards" +
            " if it does not find it returns a not found")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Card.class)))),
            @ApiResponse(responseCode = "404",description = "We did not found any cards associated with the account ID.",content = @Content(schema = @Schema(implementation = Void.class))),
    })
    @GetMapping("accounts/{id}")
    public ResponseEntity<List<Card>> searchCardsByAccountId (@PathVariable Long id ) throws ResourceNotFountException {
        Optional<List<Card>> cardsSearched = cardService.findAllCardsByAccountId(id);
        return cardsSearched.map(ResponseEntity::ok).orElseGet(()-> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
    @Operation(summary = "Get a Card by Id ",description = "Obtain the information of an existing card from the database, if it does not find it, it returns a not found")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = Card.class))),
            @ApiResponse(responseCode = "404",description = "We don´t found any card associated with the id :  + cardId ",content = @Content(schema = @Schema(implementation = Void.class))),
    })
    @GetMapping("/{id}")
    public ResponseEntity<Card> findCardById(@PathVariable Long id) throws ResourceNotFountException {
       return ResponseEntity.ok(cardService.findCardById(id));
    }
    @Operation(summary = "Delete a Card by Id ",description = "Delete the information of an existing card from the database, if it does not find it, it returns a bad request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",description = "We delete the card with id : + cardId"),
            @ApiResponse(responseCode = "400",description = "We can´t delete the card with id : + cardId  because the card does not  exist ",content = @Content(schema = @Schema(implementation = Void.class))),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) throws BadRequestException {
        cardService.deleteCardById(id);
      return ResponseEntity.ok("We delete the card with id : " +id);
    }
}