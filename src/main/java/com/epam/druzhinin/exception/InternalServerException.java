package com.epam.druzhinin.exception;

import org.springframework.http.HttpStatus;

public class InternalServerException extends BaseMvcException{
    public InternalServerException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
