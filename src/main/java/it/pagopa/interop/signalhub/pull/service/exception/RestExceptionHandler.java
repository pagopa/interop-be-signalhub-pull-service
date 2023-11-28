package it.pagopa.interop.signalhub.pull.service.exception;

import com.fasterxml.jackson.databind.JsonMappingException;

import it.pagopa.interop.signalhub.pull.service.rest.v1.dto.Problem;
import it.pagopa.interop.signalhub.pull.service.rest.v1.dto.ProblemError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(JsonMappingException.class)
    public void handle(JsonMappingException e) {
        log.error("Returning HTTP 400 Bad Request {}", e.getMessage());
    }

    @ExceptionHandler(PDNDGenericException.class)
    public Mono<ResponseEntity<Problem>> handleResponseEntityException(final PDNDGenericException exception){
        log.warn(exception.toString());

        final Problem problem = new Problem();
        if (exception.getExceptionType() == ExceptionTypeEnum.SIGNALID_ALREADY_EXISTS || exception.getExceptionType() == ExceptionTypeEnum.SIZE_NOT_VALID ) {
            problem.setTitle(HttpStatus.BAD_REQUEST.name());
            problem.setDetail(HttpStatus.valueOf(HttpStatus.BAD_REQUEST.value()).getReasonPhrase());
        }else {
            problem.setTitle(HttpStatus.FORBIDDEN.name());
            problem.setDetail(HttpStatus.valueOf(HttpStatus.FORBIDDEN.value()).getReasonPhrase());
        }
        problem.setStatus(exception.getHttpStatus().value());

        ProblemError problemError= new ProblemError();
        problemError.setCode(exception.getExceptionType().getTitle());
        problemError.setDetail(exception.getMessage());

        List<ProblemError> errors= new ArrayList<>();
        errors.add(problemError);

        problem.setErrors(errors);
        return Mono.just(ResponseEntity.status(exception.getHttpStatus()).body(problem));
    }

}
