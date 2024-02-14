package it.pagopa.interop.signalhub.pull.service.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class JWTExceptionTest {

    @Test
    void getMessage() {
        assertDoesNotThrow(() -> new JWTException(ExceptionTypeEnum.JWT_EMPTY   , "message", HttpStatus.BAD_REQUEST, "jwt"));
    }
}