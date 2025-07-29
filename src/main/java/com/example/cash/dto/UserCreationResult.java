package com.example.cash.dto;

import com.example.cash.domain.User;

public class UserCreationResult {

    private User user;

    private String errorMessage;

    public static UserCreationResult success(User user) {
        UserCreationResult result = new UserCreationResult();
        result.user = user;
        return result;
    }

    public static UserCreationResult error(String message) {
        UserCreationResult result = new UserCreationResult();
        result.errorMessage = message;
        return result;
    }

    public boolean isSuccess() {
        return user != null;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
