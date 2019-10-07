package de.adorsys.registry.manager.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class ExceptionAdvisor {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map> handleRestException(Exception ex) {
        return ResponseEntity
                       .status(HttpStatus.INTERNAL_SERVER_ERROR)
                       .build();
    }
}
