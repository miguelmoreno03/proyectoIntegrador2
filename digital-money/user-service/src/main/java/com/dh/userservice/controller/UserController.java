package com.dh.userservice.controller;

import com.dh.userservice.entities.AppUserAccountDTO;
import com.dh.userservice.entities.AppUserDTO;
import com.dh.userservice.entities.AppUserResponseDTO;
import com.dh.userservice.exceptions.BadRequestException;
import com.dh.userservice.exceptions.ResourceNotFountException;
import com.dh.userservice.entities.AppUser;
import com.dh.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserService userService;
    @GetMapping("/email/{email}")
    public ResponseEntity<AppUser> searchUser (@PathVariable String email ) throws ResourceNotFountException {
        return ResponseEntity.ok(userService.searchUserByEmail(email));
    }
    @PostMapping
    public ResponseEntity <AppUserResponseDTO> createUser (@RequestBody AppUser user)  throws BadRequestException, IOException {
        return ResponseEntity.ok(userService.createUser(user));
    }
    @GetMapping("/{id}")
    public ResponseEntity<AppUserResponseDTO> searchUser (@PathVariable Long id ) throws ResourceNotFountException {
        try {
            AppUserResponseDTO userSearched = userService.searchUserById(id);
            return ResponseEntity.ok(userSearched);
        } catch (ResourceNotFountException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @GetMapping("/{id}/account")
    public ResponseEntity<AppUserAccountDTO> searchUserWithAccountInfo (@PathVariable Long id) {
        try {
            AppUserAccountDTO userSearched = userService.findUserWithAccountInformation(id);
            return ResponseEntity.ok(userSearched);
        } catch (ResourceNotFountException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

    }
    @PatchMapping
    public ResponseEntity <AppUserResponseDTO> patchUser (@RequestBody AppUserDTO user) throws BadRequestException {
        return ResponseEntity.ok(userService.patchUser(user));

    }
}
