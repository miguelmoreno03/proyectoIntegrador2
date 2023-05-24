package com.dh.userservice.controller;

import com.dh.userservice.Exceptions.BadRequestException;
import com.dh.userservice.Exceptions.ResourceNotFountException;
import com.dh.userservice.entities.AppUser;
import com.dh.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/{email}")
    public ResponseEntity<AppUser> searchUser (@PathVariable String email ) throws ResourceNotFountException {
        return ResponseEntity.ok(userService.searchUserByEmail(email));
    }
    @PostMapping
    public ResponseEntity <AppUser> createUser (@RequestBody AppUser user)  throws BadRequestException, IOException {
        return ResponseEntity.ok(userService.createUser(user));
    }
}
