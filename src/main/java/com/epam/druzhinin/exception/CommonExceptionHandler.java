package com.epam.druzhinin.exception;

import com.epam.druzhinin.dto.MessageDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class CommonExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {BaseException.class})
    public ResponseEntity<Object> handleException(BaseException ex, WebRequest request) {
        String message = ex.getMessage();
        log.warn(message);
        return ResponseEntity.status(ex.getHttpStatus()).body(MessageDto.of(message));
    }

}
