package com.example.cash.service;

import com.example.cash.domain.User;

public interface VerificationTokenService {

    public void createVerificationToken(User user, String token);

    public String validateVerifactionToken(String token);
}
