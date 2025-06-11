package com.example.cash.web;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cash.dto.UserCreateDTO;
import com.example.cash.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthenticationResource {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register/user")
    @CrossOrigin("http://localhost:5173")
    public ResponseEntity<Void> createNewUser(@RequestBody UserCreateDTO dto) throws URISyntaxException {
        userService.createNewUser(dto);
        return ResponseEntity.created(new URI("/register/user")).build();
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserCreateDTO dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return ResponseEntity.ok("User Login");
    }
}
