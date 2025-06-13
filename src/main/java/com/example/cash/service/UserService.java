package com.example.cash.service;

import com.example.cash.dto.UserCreateDTO;

public interface UserService {
    public String createNewUser(UserCreateDTO dto);

    public UserCreateDTO findByEmail(String email);
    
    public void updateUser(String email, UserCreateDTO dto);
}
