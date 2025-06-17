package com.example.cash.web;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.cash.dto.AccountDTO;
import com.example.cash.service.AccountService;



@RestController
@RequestMapping("/api")
public class AccountResource {

    @Autowired
    private AccountService accountService;

    @PostMapping("/add/account")
    public ResponseEntity<String> addAccount(@RequestBody AccountDTO dto) throws URISyntaxException {
        accountService.addAccount(dto);
        return ResponseEntity.created(new URI("/add/account")).build();
    }

    @PutMapping("/edit/account/{id}")
    public ResponseEntity<Void> editAccount(@PathVariable Long id, @RequestBody AccountDTO dto) {
        accountService.editAccount(dto, id);        
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/account/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id){
        accountService.deleteAccount(id);
        return ResponseEntity.ok().build();
    }
    
}
