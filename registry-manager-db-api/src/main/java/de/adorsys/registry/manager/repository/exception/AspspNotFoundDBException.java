package de.adorsys.registry.manager.repository.exception;

public class AspspNotFoundDBException extends RuntimeException {

    public AspspNotFoundDBException() {
    }

    public AspspNotFoundDBException(String message) {
        super(message);
    }

    public AspspNotFoundDBException(String message, Throwable cause) {
        super(message, cause);
    }
}
