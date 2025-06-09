package com.example.cash.web;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.cash.dto.UserCreateDTO;
import com.example.cash.service.UserService;


@RestController
public class UserResource {

    @Autowired
    private UserService userService;

    @PostMapping("/register/user")
    public ResponseEntity<Void> createNewUser(@RequestBody UserCreateDTO dto) throws URISyntaxException {
        userService.createNewUser(dto);
        return ResponseEntity.created(new URI("/register/user")).build();
    }
    
}
