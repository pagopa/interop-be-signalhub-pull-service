package it.pagopa.interop.signalhub.pull.service.exception;

import com.fasterxml.jackson.databind.JsonMappingException;

import it.pagopa.interop.signalhub.pull.service.rest.v1.dto.Problem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;


@Slf4j
@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(JsonMappingException.class)
    public void handle(JsonMappingException e) {
        log.error("Returning HTTP 400 Bad Request {}", e.getMessage());
    }

    @ExceptionHandler(PocGenericException.class)
    public Mono<ResponseEntity<Problem>> handleResponseEntityException(final PocGenericException exception){
        log.warn(exception.toString());
        final Problem problem = new Problem();
        problem.setTitle(exception.getExceptionType().getTitle());
        problem.setDetail(exception.getMessage());
        problem.setStatus(exception.getHttpStatus().value());
        return Mono.just(ResponseEntity.status(exception.getHttpStatus()).body(problem));
    }

}
