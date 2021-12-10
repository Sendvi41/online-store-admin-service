package com.epam.druzhinin.exception;

import com.epam.druzhinin.dto.MessageDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Log4j2
public class CommonExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {BaseException.class})
    public ResponseEntity<Object> handleException(BaseException ex, WebRequest request) {
        String message = ex.getMessage();
        log.warn(message);
        return ResponseEntity.status(ex.getHttpStatus()).body(MessageDTO.of(message));
    }

}
