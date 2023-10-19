package it.pagopa.interop.signalhub.pull.service.filter;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import it.pagopa.interop.signalhub.pull.service.auth.JWTAuthManager;
import it.pagopa.interop.signalhub.pull.service.exception.PDNDGenericException;
import it.pagopa.interop.signalhub.pull.service.repository.JWTRepository;
import it.pagopa.interop.signalhub.pull.service.repository.cache.model.JWTCache;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JWTFilterTest {

    @Mock
    private  Function<ServerWebExchange, Mono<DecodedJWT>> jwtDecoded;

    @Mock
    private  ReactiveAuthenticationManager reactiveAuthManager = new JWTAuthManager();

    @Mock
    private  ServerSecurityContextRepository securityContextRepository = NoOpServerSecurityContextRepository.getInstance();

    @Mock
    private ServerAuthenticationSuccessHandler authSuccessHandler;

    @Mock
    private JwkProvider jwkProvider;

    @Mock
    private JWTRepository jwtRepository;

    @InjectMocks
    private JWTFilter jwtFilter;

    @Test
    void filterTest() throws JwkException {
        DecodedJWT jwt = JWT.decode("eyJ0eXAiOiJhdCtqd3QiLCJhbGciOiJSUzI1NiIsInVzZSI6InNpZyIsImtpZCI6IjMyZDhhMzIxLTE1NjgtNDRmNS05NTU4LWE5MDcyZjUxOWQyZCJ9.eyJvcmdhbml6YXRpb25JZCI6Ijg0ODcxZmQ0LTJmZDctNDZhYi05ZDIyLWY2YjQ1MmY0YjNjNSIsImF1ZCI6InVhdC5pbnRlcm9wLnBhZ29wYS5pdC9tMm0iLCJzdWIiOiIxMTM2ODUyNy00OTRiLTQyNDQtOTcwNi1iNzgyYjU0NDM4MzEiLCJyb2xlIjoibTJtIiwibmJmIjoxNjk3MTAxOTM3LCJpc3MiOiJ1YXQuaW50ZXJvcC5wYWdvcGEuaXQiLCJleHAiOjE2OTcxMDI1MzcsImlhdCI6MTY5NzEwMTkzNywiY2xpZW50X2lkIjoiMTEzNjg1MjctNDk0Yi00MjQ0LTk3MDYtYjc4MmI1NDQzODMxIiwianRpIjoiOTUzNGUwNjUtZTU2Mi00ODI3LTk2MjYtMWI4YjZiNWE0NzYxIn0.xHqqmq0Nd6tZIsu9nnAdawkNl2fA_2shnyLGiDKjmAYvp3V83gZQn64nOhD1gr_9RKceuVhC-hr39v1TntNQHKDjd93JoNPdyBm96ALif_mqxFs3IAdMpQqqxNAbAI2d7dnqpnxzRV8MRojbVCp4nuViBkGFZa6WMAgPD6dW22R_PIXJA1WYEEM3Z3qupzmsbDVfW13bnbZTqjcDMrCLwozkFIGy9qTcH4oXTlVJLF5xIyHJtVwERIMxR0jxql4Gpo9ix8DHz5mKSRUns8gfvakcrUYsmE__2ggrklUsFVBEVgcrC5q7KrLGMUvIunuPv72vkYFPA30x-NhuYxsg4w");

        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest
                        .get("/your-url")
                        .header("Authorization", "Bearer ".concat(jwt.getToken())));

        WebFilterChain filterChain = filterExchange -> Mono.empty();

        Mockito.when(jwtRepository.findByJWT(Mockito.any())).thenReturn(Mono.just(jwt ));
        Mockito.when(jwkProvider.get(Mockito.any())).thenReturn(new Jwk("1","1","12", "1",new ArrayList<>(),"1", new ArrayList<>(), "1", new HashMap<>()));
        Mockito.when(jwtRepository.saveOnCache(Mockito.any())).thenReturn(Mono.just(new JWTCache()));

        PDNDGenericException thrown = assertThrows(
                PDNDGenericException.class,
                () -> {
                    jwtFilter.filter(exchange,filterChain).block();
                }
        );
        assertEquals("Il vaucher passato non è valido", thrown.getMessage());


    }


}