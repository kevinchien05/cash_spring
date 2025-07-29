package com.example.cash.service;

import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.example.cash.domain.User;
import com.example.cash.dto.UserCreateDTO;
import com.example.cash.dto.UserCreationResult;

public interface UserService {
    public UserCreationResult createNewUser(UserCreateDTO dto);

    public UserCreateDTO findByEmail(String email);
    
    public void updateUser(String email, UserCreateDTO dto);

    public List<UserCreateDTO> findAllUser();

    public String updateUserInfo(Long id, UserCreateDTO dto);

    public String addNewProfile(Long id,MultipartFile pic);

    public String editProfile(Long id, MultipartFile pic);

    public String validateVerificationToken(String token);
}
