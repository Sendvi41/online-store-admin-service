package com.epam.druzhinin.exception;


import org.springframework.http.HttpStatus;

public class NotFoundException extends BaseMvcException {
    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}

