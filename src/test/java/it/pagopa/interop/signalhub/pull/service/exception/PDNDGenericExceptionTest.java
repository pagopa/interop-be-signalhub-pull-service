package it.pagopa.interop.signalhub.pull.service.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class PDNDGenericExceptionTest {

    @Test
    void getMessage() {
        assertDoesNotThrow(() -> new PDNDGenericException(ExceptionTypeEnum.UNAUTHORIZED, "message"));
        assertDoesNotThrow(() -> new PDNDGenericException(ExceptionTypeEnum.UNAUTHORIZED, "message", HttpStatus.MULTI_STATUS));

    }

}