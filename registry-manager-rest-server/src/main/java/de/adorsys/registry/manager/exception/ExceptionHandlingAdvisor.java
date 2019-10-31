package de.adorsys.registry.manager.exception;

import de.adorsys.registry.manager.service.exception.IbanException;
import de.adorsys.registry.manager.service.model.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.UncheckedIOException;
import java.util.List;

@ControllerAdvice
public class ExceptionHandlingAdvisor {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlingAdvisor.class);
    private static final String IBAN_EXCEPTION_ERROR_MESSAGE = "Exception during the IBAN processing: IBAN is incorrect";
    private static final String UNCHECKED_IO_EXCEPTION_ERROR_MESSAGE = "Exception during the IO process";
    private static final String ACCESS_DENIED_EXCEPTION_ERROR_MESSAGE = "Access denied";
    private static final String EXCEPTION_ERROR_MESSAGE = "Server error";

    @ExceptionHandler(IbanException.class)
    public ResponseEntity<ErrorResponse> handle(IbanException ex) {
        return handle(ex, IBAN_EXCEPTION_ERROR_MESSAGE, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UncheckedIOException.class)
    public ResponseEntity<ErrorResponse> handle(UncheckedIOException ex) {
        return handle(ex, UNCHECKED_IO_EXCEPTION_ERROR_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handle(AccessDeniedException ex) {
        return handle(ex, ACCESS_DENIED_EXCEPTION_ERROR_MESSAGE, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handle(Exception ex) {
        return handle(ex, EXCEPTION_ERROR_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponse> handle(Exception ex, String errorMessage, HttpStatus httpStatus) {
        logError(ex);
        return ResponseEntity
                       .status(httpStatus)
                       .body(new ErrorResponse(List.of(errorMessage)));
    }

    private void logError(Exception exception) {
        String errorMessage = exception.getMessage() == null ? "" : exception.getMessage();
        logger.error(errorMessage, exception);
    }
}
