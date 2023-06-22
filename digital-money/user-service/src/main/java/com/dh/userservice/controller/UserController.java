package com.dh.userservice.controller;

import com.dh.userservice.entities.AppUserAccountDTO;
import com.dh.userservice.entities.AppUserDTO;
import com.dh.userservice.entities.AppUserResponseDTO;
import com.dh.userservice.exceptions.BadRequestException;
import com.dh.userservice.exceptions.ResourceNotFountException;
import com.dh.userservice.entities.AppUser;
import com.dh.userservice.service.UserService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@OpenAPIDefinition(info = @Info(title = "User-Service-API",version = "1.0.0",description = "API to manage the users"))
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserService userService;
    @Operation(summary = "Get a user by  email",description = "Obtain the information of an existing user from the database, if it does not find it, it returns a not found")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = AppUserResponseDTO.class))),
            @ApiResponse(responseCode = "404",description = "We don´t found any user with the email : + email",content = @Content(schema = @Schema(implementation = Void.class))),
    })
    @GetMapping("/email/{email}")
    public ResponseEntity<AppUserResponseDTO> searchUserByEmail (@PathVariable String email ) throws ResourceNotFountException {
            return ResponseEntity.ok(userService.searchUserByEmail(email));
    }
    @Operation(summary = "Create a user ",description = "create a user both in keycloak and in my database from the information provided in the body")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = AppUserResponseDTO.class))),
            @ApiResponse(responseCode = "400",description = "This email is already associated with a user created ",content = @Content(schema = @Schema(implementation = Void.class)))
    })
    @PostMapping
    public ResponseEntity <AppUserResponseDTO> createUser (@RequestBody AppUser user)  throws BadRequestException{
        return ResponseEntity.ok(userService.createUser(user));
    }
    @Operation(summary = "Get a User by Id ",description = "Obtain the information of an existing user from the database, if it does not find it, it returns a not found")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = AppUserResponseDTO.class))),
            @ApiResponse(responseCode = "404",description = "We don´t found any user with the id  + userId ",content = @Content(schema = @Schema(implementation = Void.class))),
    })
    @GetMapping("/{id}")
    public ResponseEntity<AppUserResponseDTO> searchUser (@PathVariable Long id ) throws ResourceNotFountException {
            return ResponseEntity.ok(userService.searchUserById(id));
    }
    @Operation(summary = "Get a User by Id with the account information  ",description = "Obtain the information of an existing user and the Account  information , if it does not find it, it returns a not found")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",content = @Content(schema = @Schema(implementation = AppUserAccountDTO.class))),
            @ApiResponse(responseCode = "404",description = "We don't found any account associated  with this user id:  + userId <br> <br> " +
                    "We don't found any user with the id:  + userId",content = @Content(schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "500",description = "We have problems  with the account-service try later",content = @Content(schema = @Schema(implementation = Void.class))),

    })
    @GetMapping("/{id}/account")
    public ResponseEntity<AppUserAccountDTO> searchUserWithAccountInfo (@PathVariable Long id) throws ResourceNotFountException {
        return ResponseEntity.ok(userService.findUserWithAccountInformation(id));
    }
    @Operation(summary = "Edit a user by his id",description = "Update and Get the information of an existing user from the database, if it does not find it, it returns a not found")
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = @Content(schema = @Schema(implementation = AppUserResponseDTO.class))),
            @ApiResponse(responseCode = "400",description = "We can´t update the user because the user don´t exist ",content = @Content(schema = @Schema(implementation = Void.class)))
    })
    @PatchMapping
    public ResponseEntity <AppUserResponseDTO> patchUser (@RequestBody AppUserDTO user) throws BadRequestException {
        return ResponseEntity.ok(userService.patchUser(user));

    }
    @ExceptionHandler(ResourceNotFountException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFountException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }


}
