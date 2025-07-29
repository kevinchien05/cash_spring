package com.example.cash.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.cash.domain.User;
import com.example.cash.repository.UserRepository;
import com.example.cash.service.VerificationTokenService;

@Service
public class VerificationTokenServiceImpl implements VerificationTokenService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void createVerificationToken(User user, String token) {
        user.setVerificationToken(token);
        userRepository.save(user);
    }

    @Override
    public String validateVerifactionToken(String token) {
        User user = userRepository.findByVerificationToken(token).orElse(null);

        if(user == null){
            return "invalid";
        }

        user.setVerified(true);
        userRepository.save(user);
        return "valid";
    }

}
