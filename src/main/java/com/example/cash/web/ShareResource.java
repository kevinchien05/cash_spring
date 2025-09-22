package com.example.cash.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.cash.domain.User;
import com.example.cash.dto.AccountDTO;
import com.example.cash.service.ShareService;


@RestController
@RequestMapping("/api")
public class ShareResource {

    @Autowired
    private ShareService shareService;

    @GetMapping("/share/account")
    public ResponseEntity<List<AccountDTO>> getSharedAccount(@RequestParam Long id) {
        List<AccountDTO> dtos = shareService.getSharedAccount(id);
        return ResponseEntity.ok(dtos);
    }
    
    @GetMapping("/share/user")
    public ResponseEntity<List<User>> getSharedUser(@RequestParam Long id) {
        List<User> dtos = shareService.getSharedUser(id);
        return ResponseEntity.ok(dtos);
    }
    
    @DeleteMapping("/delete/share/{user}/{account}")
    public ResponseEntity<Void> deleteShare(@PathVariable Long user, @PathVariable Long account){
        shareService.deleteShare(account, user);
        return ResponseEntity.ok().build();
    }
}
