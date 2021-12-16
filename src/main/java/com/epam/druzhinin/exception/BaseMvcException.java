package com.epam.druzhinin.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BaseMvcException extends RuntimeException {
    private String message;
    private HttpStatus httpStatus;

    public BaseMvcException(String message, HttpStatus httpStatus) {
        super(message);
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
