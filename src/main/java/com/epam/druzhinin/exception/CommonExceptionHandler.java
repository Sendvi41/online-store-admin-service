package com.epam.druzhinin.exception;

import com.epam.druzhinin.dto.MessageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.UUID;

@ControllerAdvice
@Slf4j
public class CommonExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {BaseMvcException.class})
    public ResponseEntity<Object> handleException(BaseMvcException ex, WebRequest request) {
        String message = ex.getMessage();
        log.warn(message);
        return ResponseEntity.status(ex.getHttpStatus()).body(MessageDto.of(message));
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleException(Exception ex) {
        UUID uuid = UUID.randomUUID();
        log.error("Uncaught error occurred [incidentId={}]", uuid, ex);
        String errorMessage = "Internal server error, incident id=" + uuid;
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(MessageDto.of(errorMessage));
    }
}
