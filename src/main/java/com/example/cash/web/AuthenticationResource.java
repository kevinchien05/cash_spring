package com.example.cash.web;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.cash.dto.UserCreateDTO;
import com.example.cash.dto.UserCreationResult;
import com.example.cash.dto.UserJoinSettingDTO;
import com.example.cash.repository.UserRepository;
import com.example.cash.service.UserService;

@RestController
@RequestMapping("/auth")
public class AuthenticationResource {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    @Lazy
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/register/user")
    public ResponseEntity<String> createNewUser(@RequestBody UserCreateDTO dto) throws URISyntaxException {
        UserCreationResult result = userService.createNewUser(dto);
        if (result.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body("success");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(result.getErrorMessage());
        }
    }

    @GetMapping("/verify-email")
    public ResponseEntity<Map<String, String>> verifyEmail(@RequestParam("token") String token) {
        String result = userService.validateVerificationToken(token);

        Map<String, String> response = new HashMap<>();
        if ("valid".equals(result)) {
            response.put("status", "success");
            response.put("message", "Your account has been verified successfully.");
            return ResponseEntity.ok(response);
        } else {
            response.put("status", "error");
            response.put("message", "Invalid or expired verification token.");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/forget/email")
    public ResponseEntity<?> findEmail(@RequestBody UserCreateDTO dto) {
        UserCreateDTO dtoUser = userService.findByEmail(dto.getEmail().toLowerCase());
        if (dtoUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Email not found");
        }
        return ResponseEntity.ok(dtoUser);
    }

    @PutMapping("/update/user/{email}")
    public ResponseEntity<Void> updateUser(@PathVariable String email, @RequestBody UserCreateDTO dto) {
        userService.updateUser(email, dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user")
    public ResponseEntity<?> getCurrentUser(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(401).body("Not authenticated");
        }
        // User user = userRepository.findByEmail(auth.getName());
        UserJoinSettingDTO user = userRepository.findUserJoinSetting(auth.getName());
        return ResponseEntity.ok(user); // or return UserDTO
    }

    @GetMapping("/reset/password")
    public ResponseEntity<?> resetVerification(@RequestParam String email, @RequestParam String token) {
        userService.resetVerification(email, token);
        return ResponseEntity.ok("Success");
    }
    
}
