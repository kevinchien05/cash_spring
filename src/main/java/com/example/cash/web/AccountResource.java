package com.example.cash.web;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
        String result = accountService.addAccount(dto);
        if ("Account created".equals(result)) {
            return ResponseEntity.created(new URI("/add/account")).build();
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
        }
    }

    @PutMapping("/edit/account/{id}")
    public ResponseEntity<?> editAccount(@PathVariable Long id, @RequestBody AccountDTO dto) {
        String result = accountService.editAccount(dto, id);
        if ("Account edited".equals(result)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
        }
    }

    @DeleteMapping("/delete/account/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<AccountDTO>> getAllAccountByUserID(@RequestParam(value="id", required=true) Long id, @RequestParam(value="name", required=false) String name) {
        List<AccountDTO> dtos = accountService.findAccountByUser(id, name);
        return ResponseEntity.ok(dtos);
    }

}
