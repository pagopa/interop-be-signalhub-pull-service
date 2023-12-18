package it.pagopa.interop.signalhub.pull.service.exception;

public class PDNDKeyFactoryException extends RuntimeException {

    public PDNDKeyFactoryException(String message) {
        super(message);
    }

    public PDNDKeyFactoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
