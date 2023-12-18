package it.pagopa.interop.signalhub.pull.service.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class PDNDExceptionHelperTest {

    @Test
    void handle() {
        PDNDExceptionHelper pdndExceptionHelper= new PDNDExceptionHelper();
        assertNotNull(pdndExceptionHelper.handle(new Throwable()));
        assertNotNull(pdndExceptionHelper.handle(new PDNDGenericException(ExceptionTypeEnum.UNAUTHORIZED, ExceptionTypeEnum.UNAUTHORIZED.getMessage())));
        assertNotNull(pdndExceptionHelper.handle(new JWTException(ExceptionTypeEnum.JWT_NOT_VALID, ExceptionTypeEnum.JWT_NOT_VALID.getMessage(), HttpStatus.BAD_REQUEST, "jwt")));
    }

    @Test
    void generateFallbackProblem() {
        PDNDExceptionHelper pdndExceptionHelper= new PDNDExceptionHelper();
        assertNotNull(pdndExceptionHelper.generateFallbackProblem());
    }
}