package de.adorsys.registry.manager.exception;

import de.adorsys.registry.manager.service.exception.IbanException;
import de.adorsys.registry.manager.service.model.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.UncheckedIOException;
import java.util.List;

@ControllerAdvice
public class ExceptionHandlingAdvisor {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlingAdvisor.class);

    @ExceptionHandler(IbanException.class)
    public ResponseEntity<ErrorResponse> handle(IbanException ex) {
        logError(ex);
        String errorMessage = "Exception during the IBAN processing: IBAN is incorrect";
        return ResponseEntity
                       .status(HttpStatus.BAD_REQUEST)
                       .body(new ErrorResponse(List.of(errorMessage)));
    }

    @ExceptionHandler(UncheckedIOException.class)
    public ResponseEntity<ErrorResponse> handle(UncheckedIOException ex) {
        logError(ex);
        String errorMessage = "Exception during the IO process";
        return ResponseEntity
                       .status(HttpStatus.INTERNAL_SERVER_ERROR)
                       .body(new ErrorResponse(List.of(errorMessage)));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handle(Exception ex) {
        logError(ex);
        String errorMessage = "Server error";
        return ResponseEntity
                       .status(HttpStatus.INTERNAL_SERVER_ERROR)
                       .body(new ErrorResponse(List.of(errorMessage)));
    }

    private void logError(Exception exception) {
        String errorMessage = exception.getMessage() == null ? "" : exception.getMessage();
        logger.error(errorMessage, exception);
    }
}
