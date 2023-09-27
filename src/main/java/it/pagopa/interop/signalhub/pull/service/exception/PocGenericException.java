package it.pagopa.interop.signalhub.pull.service.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public class PocGenericException extends RuntimeException {
    private final ExceptionTypeEnum exceptionType;
    private final HttpStatus httpStatus;
    private final String message;


    public PocGenericException(ExceptionTypeEnum exceptionType, String message){
        super(message);
        this.exceptionType = exceptionType;
        this.message = message;
        this.httpStatus = HttpStatus.BAD_REQUEST;
    }

    public PocGenericException(ExceptionTypeEnum exceptionType, String message, HttpStatus status){
        super(message);
        this.exceptionType = exceptionType;
        this.message = message;
        this.httpStatus = status;
    }


}
