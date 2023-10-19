package it.pagopa.interop.signalhub.pull.service.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import reactor.core.publisher.Mono;

import static it.pagopa.interop.signalhub.pull.service.exception.ExceptionTypeEnum.CORRESPONDENCE_NOT_FOUND;


class RestExceptionHandlerTest {
    @Spy
    private RestExceptionHandler restExceptionHandler;


    @BeforeEach
    void setUp(){
        this.initialize();
    }

    @Test
    void handleResponseEntityExceptionTest(){
        PDNDGenericException pnGenericException = new PDNDGenericException(CORRESPONDENCE_NOT_FOUND, CORRESPONDENCE_NOT_FOUND.getMessage());
        restExceptionHandler.handleResponseEntityException(pnGenericException)
                .map(responseEntity -> {
                    Assertions.assertEquals(CORRESPONDENCE_NOT_FOUND.getTitle(), responseEntity.getBody().getTitle());
                    Assertions.assertEquals(CORRESPONDENCE_NOT_FOUND.getMessage(), responseEntity.getBody().getDetail());
                    return Mono.empty();
                })
                .block();
    }

    private void initialize() {
        restExceptionHandler = new RestExceptionHandler();
    }
}
