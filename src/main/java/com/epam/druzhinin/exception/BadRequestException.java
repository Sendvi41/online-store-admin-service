package com.epam.druzhinin.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends BaseMvcException {
    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
