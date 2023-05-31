package com.dh.userservice.exceptions;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalException {
    private static final Logger logger = Logger.getLogger(GlobalException.class);

    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<String> processErrorBadRequest(BadRequestException ex) {
        logger.error(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
