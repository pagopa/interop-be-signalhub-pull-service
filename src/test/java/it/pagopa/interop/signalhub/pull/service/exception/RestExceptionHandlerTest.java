package it.pagopa.interop.signalhub.pull.service.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.http.HttpStatus;
import reactor.core.publisher.Mono;

import static it.pagopa.interop.signalhub.pull.service.exception.ExceptionTypeEnum.UNAUTHORIZED;


class RestExceptionHandlerTest {
    @Spy
    private RestExceptionHandler restExceptionHandler;


    @BeforeEach
    void setUp(){
        this.initialize();
    }

    @Test
    void handleResponseEntityExceptionTest(){
        PDNDGenericException pnGenericException = new PDNDGenericException(UNAUTHORIZED, UNAUTHORIZED.getMessage());
        restExceptionHandler.handleResponseEntityException(pnGenericException)
                .map(responseEntity -> {
                    Assertions.assertEquals(HttpStatus.FORBIDDEN.name(), responseEntity.getBody().getTitle());
                    Assertions.assertEquals(HttpStatus.valueOf(HttpStatus.FORBIDDEN.value()).getReasonPhrase(), responseEntity.getBody().getDetail());
                    return Mono.empty();
                })
                .block();
    }

    private void initialize() {
        restExceptionHandler = new RestExceptionHandler();
    }
}
