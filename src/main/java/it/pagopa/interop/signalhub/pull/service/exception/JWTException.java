package it.pagopa.interop.signalhub.pull.service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public class JWTException extends RuntimeException {
    private final ExceptionTypeEnum exceptionType;
    private final HttpStatus httpStatus;
    private final String message;
    private final String jwt;


    public JWTException(ExceptionTypeEnum exceptionType, String message, HttpStatus status, String jwt){
        super(message);
        this.exceptionType = exceptionType;
        this.message = message;
        this.httpStatus = status;
        this.jwt = jwt;
    }

}
