package it.pagopa.interop.signalhub.pull.service.exception;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.pagopa.interop.signalhub.pull.service.rest.v1.dto.Problem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class PDNDErrorWebExceptionHandlerTest {

    @InjectMocks
    private PDNDErrorWebExceptionHandler pdndErrorWebExceptionHandler;

    @Mock
    private PDNDExceptionHelper helper;

    @Mock
    private ObjectMapper objectMapper;

    @Test
    void handle() throws JsonProcessingException {

        DecodedJWT jwt = JWT.decode("eyJ0eXAiOiJhdCtqd3QiLCJhbGciOiJSUzI1NiIsInVzZSI6InNpZyIsImtpZCI6IjMyZDhhMzIxLTE1NjgtNDRmNS05NTU4LWE5MDcyZjUxOWQyZCJ9.eyJvcmdhbml6YXRpb25JZCI6Ijg0ODcxZmQ0LTJmZDctNDZhYi05ZDIyLWY2YjQ1MmY0YjNjNSIsImF1ZCI6InVhdC5pbnRlcm9wLnBhZ29wYS5pdC9tMm0iLCJzdWIiOiIxMTM2ODUyNy00OTRiLTQyNDQtOTcwNi1iNzgyYjU0NDM4MzEiLCJyb2xlIjoibTJtIiwibmJmIjoxNjk3MTAxOTM3LCJpc3MiOiJ1YXQuaW50ZXJvcC5wYWdvcGEuaXQiLCJleHAiOjE2OTcxMDI1MzcsImlhdCI6MTY5NzEwMTkzNywiY2xpZW50X2lkIjoiMTEzNjg1MjctNDk0Yi00MjQ0LTk3MDYtYjc4MmI1NDQzODMxIiwianRpIjoiOTUzNGUwNjUtZTU2Mi00ODI3LTk2MjYtMWI4YjZiNWE0NzYxIn0.xHqqmq0Nd6tZIsu9nnAdawkNl2fA_2shnyLGiDKjmAYvp3V83gZQn64nOhD1gr_9RKceuVhC-hr39v1TntNQHKDjd93JoNPdyBm96ALif_mqxFs3IAdMpQqqxNAbAI2d7dnqpnxzRV8MRojbVCp4nuViBkGFZa6WMAgPD6dW22R_PIXJA1WYEEM3Z3qupzmsbDVfW13bnbZTqjcDMrCLwozkFIGy9qTcH4oXTlVJLF5xIyHJtVwERIMxR0jxql4Gpo9ix8DHz5mKSRUns8gfvakcrUYsmE__2ggrklUsFVBEVgcrC5q7KrLGMUvIunuPv72vkYFPA30x-NhuYxsg4w");

        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest
                        .get("/your-url")
                        .header("Authorization", "Bearer ".concat(jwt.getToken())));
        Problem problem= new Problem();
        problem.setStatus(1);

        Mockito.when(helper.handle(Mockito.any())).thenReturn(problem);
        Mockito.when(objectMapper.writeValueAsBytes(Mockito.any())).thenReturn(new byte[1]);

        assertNotNull(pdndErrorWebExceptionHandler.handle(exchange, new PDNDGenericException(ExceptionTypeEnum.UNAUTHORIZED, ExceptionTypeEnum.UNAUTHORIZED.getMessage())));
    }
}