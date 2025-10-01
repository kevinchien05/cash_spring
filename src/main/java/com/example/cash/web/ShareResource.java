package com.example.cash.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.cash.dto.ShareAccountDTO;
import com.example.cash.dto.ShareDTO;
import com.example.cash.dto.ShareUserDTO;
import com.example.cash.service.ShareService;

@RestController
@RequestMapping("/api")
public class ShareResource {

    @Autowired
    private ShareService shareService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @GetMapping("/share/account")
    public ResponseEntity<List<ShareAccountDTO>> getSharedAccount(@RequestParam Long id, @RequestParam(value = "name", required = false) String name) {
        List<ShareAccountDTO> dtos = shareService.getSharedAccount(id, name);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/share/user")
    public ResponseEntity<List<ShareUserDTO>> getSharedUser(@RequestParam Long id) {
        List<ShareUserDTO> dtos = shareService.getSharedUser(id);
        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/delete/share/{user}/{account}")
    public ResponseEntity<String> deleteShare(@PathVariable Long user, @PathVariable Long account) {
        String result = shareService.deleteShare(account, user);
        Map<String, Object> payload = new HashMap<>();
        payload.put("action", "DELETE");
        payload.put("data", account);
        messagingTemplate.convertAndSend("/topic/share/" + user, payload);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/add/share/{id}")
    public ResponseEntity<String> addShare(@RequestBody ShareDTO dto, @PathVariable Long id) {
        String result = shareService.createShare(id, dto);
        if (result.equals("Share Relationship Created")) {
            Map<String, Object> payload = new HashMap<>();
            payload.put("action", "ADD");
            ShareAccountDTO data = shareService.setShareAccountDTO(id, dto.getAccess());
            payload.put("data", data);
            messagingTemplate.convertAndSend("/topic/share/" + dto.getEmail().toLowerCase(), payload);
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(result);
        }
    }

    @PutMapping("/edit/share")
    public ResponseEntity<String> editShare(@RequestParam Long access, @RequestParam Long user, @RequestParam Long account) {
        String result = shareService.editShare(account, user, access);
        Map<String, Object> payload = new HashMap<>();
        payload.put("action", "EDIT");
        payload.put("account", account);
        payload.put("access", access);
        messagingTemplate.convertAndSend("/topic/share/" + user, payload);
        return ResponseEntity.ok(result);
    }
}
