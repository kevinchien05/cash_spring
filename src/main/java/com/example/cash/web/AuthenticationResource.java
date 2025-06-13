package com.example.cash.web;

import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
    public ResponseEntity<String> createNewUser(@RequestBody UserCreateDTO dto) throws URISyntaxException {
        String result = userService.createNewUser(dto);
        return switch (result) {
            case "Username already used", "Email already used" -> ResponseEntity.status(HttpStatus.CONFLICT).body(result);
            case "User Created" -> ResponseEntity.status(HttpStatus.CREATED).body(result);
            default -> ResponseEntity.badRequest().body("Unknown error");
        };
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserCreateDTO dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail().toLowerCase(), dto.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return ResponseEntity.ok("User Login");
    }

    @PostMapping("/forget/email")
    public ResponseEntity<UserCreateDTO> findEmail(@RequestBody UserCreateDTO dto) {
        UserCreateDTO dtoUser = userService.findByEmail(dto.getEmail().toLowerCase());
        return ResponseEntity.ok(dtoUser);
    }

    @PutMapping("/update/user/{email}")
    public ResponseEntity<Void> updateUser(@PathVariable String email, @RequestBody UserCreateDTO dto) {
        userService.updateUser(email, dto);
        return ResponseEntity.ok().build();
    }
}
