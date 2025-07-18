package com.example.cash.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.cash.domain.User;
import com.example.cash.dto.UserCreateDTO;
import com.example.cash.exception.ResourceNotFoundException;
import com.example.cash.repository.UserRepository;
import com.example.cash.service.UserService;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    @Lazy
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public String createNewUser(UserCreateDTO dto) {
        if(userRepository.findByEmail(dto.getEmail().toLowerCase()) != null){
            return "Email already used";
        }else if (userRepository.findByUsername(dto.getUsername().toLowerCase()) != null){
            return "Username already used";
        }
        User user = new User();
        user.setUsername(dto.getUsername().toLowerCase());
        user.setEmail(dto.getEmail().toLowerCase());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(dto.getRole());
        userRepository.save(user);
        return "User Created";
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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), Collections.emptyList());
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
        Boolean containsUsername = users.stream().anyMatch(userName -> userName.getUsername().equalsIgnoreCase(dto.getUsername()));
        Boolean containsEmail = users.stream().anyMatch(userEmail -> userEmail.getEmail().equalsIgnoreCase(dto.getEmail()));
        if(!"".equals(dto.getUsername()) && !containsUsername){
            user.setUsername(dto.getUsername());
        }
        if(!"".equals(dto.getEmail()) && !containsEmail){
            user.setEmail(dto.getEmail());
        }
        userRepository.save(user);
        return "Update Success";
    }


}
