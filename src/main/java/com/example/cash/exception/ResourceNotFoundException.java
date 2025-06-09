package com.example.cash.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import jakarta.validation.ConstraintDeclarationException;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends ConstraintDeclarationException {

    private static final long serialVersionUID = 7098281569398414752L;

    public ResourceNotFoundException(String message) {
        super(message);
    }

}
