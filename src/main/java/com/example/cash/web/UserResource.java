package com.example.cash.web;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/user/email")
    public ResponseEntity<UserCreateDTO> findUserByEmail(@RequestParam(value="email") String email) {
        UserCreateDTO dto = userService.findByEmail(email);    
        return ResponseEntity.ok().body(dto);
    }

    @GetMapping("/user")
    public ResponseEntity<List<UserCreateDTO>> findAllUser() {
        List<UserCreateDTO> dtos = userService.findAllUser();
        return ResponseEntity.ok(dtos);
    }
    

    @PutMapping("/update/user/{email}")
    public ResponseEntity<Void> updateUser(@PathVariable String email, @RequestBody UserCreateDTO dto) {
        userService.updateUser(email, dto);
        return ResponseEntity.ok().build();
    }
    
    
}
