package com.example.cash.service.impl;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.cash.domain.User;
import com.example.cash.dto.SettingDTO;
import com.example.cash.dto.UserCreateDTO;
import com.example.cash.dto.UserCreationResult;
import com.example.cash.event.OnRegistrationCompleteEvent;
import com.example.cash.exception.ResourceNotFoundException;
import com.example.cash.repository.UserRepository;
import com.example.cash.service.EmailService;
import com.example.cash.service.SettingService;
import com.example.cash.service.UserService;

import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    @Lazy
    private BCryptPasswordEncoder passwordEncoder;

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Autowired
    private SettingService settingService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Value("${frontend-url}")
    private String frontendUrl;

    @Transactional
    @Override
    public UserCreationResult createNewUser(UserCreateDTO dto) {
        if (userRepository.findByEmail(dto.getEmail().toLowerCase()) != null) {
            return UserCreationResult.error("Email already used");
        } else if (userRepository.findByUsername(dto.getUsername().toLowerCase()) != null) {
            return UserCreationResult.error("Username already used");
        }
        User user = new User();
        user.setUsername(dto.getUsername().toLowerCase());
        user.setEmail(dto.getEmail().toLowerCase());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(dto.getRole());
        user.setVerified(false);
        userRepository.save(user);
        
        SettingDTO setting = new SettingDTO();
        setting.setDark(false);
        settingService.addSetting(user.getId(), setting);
        
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user));

        return UserCreationResult.success(user);
    }

    @Override
    public UserCreateDTO findByEmail(String email) {
        User user = userRepository.findByEmail(email.toLowerCase());
        UserCreateDTO dto = new UserCreateDTO();
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword());
        dto.setRole(user.getRole());
        return dto;
    }

    @Override
    public void updateUser(String email, UserCreateDTO dto) {
        User user = userRepository.findByEmail(email.toLowerCase());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        userRepository.save(user);
    }

    @Override
    public List<UserCreateDTO> findAllUser() {
        List<User> users = userRepository.findAll();
        return users.stream().map((user) -> {
            UserCreateDTO dto = new UserCreateDTO();
            dto.setUsername(user.getUsername());
            dto.setEmail(user.getEmail());
            dto.setPassword(user.getPassword());
            dto.setRole(user.getRole());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public String updateUserInfo(Long id, UserCreateDTO dto) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
        List<User> users = userRepository.findAll();
        if (!dto.getUsername().equalsIgnoreCase(user.getUsername())) {
            Boolean containsUsername = users.stream().anyMatch(userName -> userName.getUsername().equalsIgnoreCase(dto.getUsername()));
            if (!"".equals(dto.getUsername()) && !containsUsername) {
                user.setUsername(dto.getUsername().toLowerCase());
            } else {
                throw new IllegalArgumentException("Username is already taken");
            }
        }
        if (!dto.getEmail().equalsIgnoreCase(user.getEmail().toLowerCase())) {
            Boolean containsEmail = users.stream().anyMatch(userEmail -> userEmail.getEmail().equalsIgnoreCase(dto.getEmail()));
            if (!"".equals(dto.getEmail()) && !containsEmail) {
                user.setEmail(dto.getEmail());
            }else{
                throw new IllegalArgumentException("Email is already taken");

            }
        }
        userRepository.save(user);
        return "Update Success";
    }

    @Override
    public String addNewProfile(Long id, MultipartFile pic) {
        try {
            // 1. Get original filename
            String originalFileName = pic.getOriginalFilename();
            String extension = originalFileName.substring(originalFileName.lastIndexOf('.'));
            String hashedFileName = hashFileName(originalFileName + System.currentTimeMillis()) + extension;

            // ✅ 2. Save to absolute path (outside of Tomcat's working directory)
            String uploadFolder = System.getProperty("user.dir") + File.separator + "profile";  // points to your project root
            File dir = new File(uploadFolder);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File dest = new File(uploadFolder + File.separator + hashedFileName);
            pic.transferTo(dest); // ✅ WILL WORK HERE

            // 3. Update user
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
            user.setImage(hashedFileName);
            userRepository.save(user);

            return "Success";
        } catch (IOException e) {
            e.printStackTrace(); // Add for debugging
            throw new RuntimeException("File upload failed", e);
        }
    }

    @Override
    public String editProfile(Long id, MultipartFile pic) {
        try {
            User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
            String uploadFolder = System.getProperty("user.dir") + File.separator + "profile";  // points to your project root
            File file = new File(uploadFolder + File.separator + user.getImage());

            if (file.exists()) {
                file.delete();
            }

            String originalFileName = pic.getOriginalFilename();
            String extension = originalFileName.substring(originalFileName.lastIndexOf('.'));
            String hashedFileName = hashFileName(originalFileName + System.currentTimeMillis()) + extension;

            File dir = new File(uploadFolder);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            File dest = new File(uploadFolder + File.separator + hashedFileName);
            pic.transferTo(dest); // ✅ WILL WORK HERE

            user.setImage(hashedFileName);
            userRepository.save(user);

            return "Success";
        } catch (IOException e) {
            e.printStackTrace(); // Add for debugging
            throw new RuntimeException("File upload failed", e);
        }
    }

    private String hashFileName(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not available", e);
        }
    }

    @Override
    public String validateVerificationToken(String token) {
        User user = userRepository.findByVerificationToken(token).orElse(null);

        if (user == null) {
            return "invalid";
        }

        user.setVerified(true);
        userRepository.save(user);
        return "valid";
    }

    @Override
    public void resetVerification(String email, String txt) {
        String encodedTxt = URLEncoder.encode(txt, StandardCharsets.UTF_8);
        String confirmationUrl = frontendUrl + "/forget/password?email=" + encodedTxt;
        emailService.sendEmail(email, "Email Verification", "Click the link to reset your password: " + confirmationUrl);
    }

}
