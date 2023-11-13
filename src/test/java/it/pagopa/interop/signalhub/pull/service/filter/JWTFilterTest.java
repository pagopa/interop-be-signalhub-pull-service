package it.pagopa.interop.signalhub.pull.service.filter;


import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import it.pagopa.interop.signalhub.pull.service.auth.JWTAuthManager;
import it.pagopa.interop.signalhub.pull.service.auth.JWTConverter;
import it.pagopa.interop.signalhub.pull.service.auth.PrincipalAgreement;
import it.pagopa.interop.signalhub.pull.service.auth.PrincipalAgreementValidator;
import it.pagopa.interop.signalhub.pull.service.config.BeanBuilder;
import it.pagopa.interop.signalhub.pull.service.service.InteropService;
import it.pagopa.interop.signalhub.pull.service.service.JWTService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class JWTFilterTest {
    @Mock
    private JWTConverter jwtConverter;
    @Mock
    private PrincipalAgreementValidator principalAgreementValidator;
    @Spy
    private ReactiveAuthenticationManager reactiveAuthManager = new JWTAuthManager();
    @Mock
    private ServerSecurityContextRepository securityContextRepository = NoOpServerSecurityContextRepository.getInstance();
    @Mock
    private ServerAuthenticationSuccessHandler authSuccessHandler;
    @Mock
    private JWTService jwtService;
    @Mock
    private InteropService interopService;
    @InjectMocks
    private JWTFilter jwtFilter;

    @Test
    void filterTest() {
        DecodedJWT jwt = JWT.decode("eyJ0eXAiOiJhdCtqd3QiLCJhbGciOiJSUzI1NiIsInVzZSI6InNpZyIsImtpZCI6IjMyZDhhMzIxLTE1NjgtNDRmNS05NTU4LWE5MDcyZjUxOWQyZCJ9.eyJhdWQiOiJpbnRlcm9wLXNpZ25hbGh1Yi1wdXNoLXNpZ25hbCIsInN1YiI6ImY3YzFhZDIwLWIwZDktNDIxMi1iMGIwLTQ2NTkyODgzNTY2MyIsIm5iZiI6MTY5OTQ1NjYzNCwicHVycG9zZUlkIjoiYjY5M2ViNmUtNzNkZC00YjU4LWE0MmEtM2UwNjRhNmE0Y2FiIiwiaXNzIjoidWF0LmludGVyb3AucGFnb3BhLml0IiwiZXhwIjoxNjk5NDU4NDM0LCJpYXQiOjE2OTk0NTY2MzQsImNsaWVudF9pZCI6ImY3YzFhZDIwLWIwZDktNDIxMi1iMGIwLTQ2NTkyODgzNTY2MyIsImp0aSI6ImFjNDVjZDE2LThmMzgtNDFhYS1hODg2LWYyZWY5OWM4ZDE1MyJ9.u0j4xsRY-8sQKRvBr-QjP6FFTwycWz7bgN6t8wyv9cSk9LZUsmOO5pxrudwVL0I5zAjo2uYjEcvNv7VYHynn01mXR1zi2vDO9Se83fQ479_4HhNroENbI7wBwfzm51teUq7cQ1f19o4CKO5esnw0RdjOpWx9yCFyNhmqcVHRhHRiRAcuyxC0E5QMGGbNcoZEljfzNy6QIruF9dUbeeHKqyF_RA-zhKCuQB7bnTDnQPyYd3mrOktYIcnGjeE1ynJJGHvCeM-P84WwbnnbSzQKYpEJHYa4TS-hRm2H3MIv1fLkAjnM8qWkjIwh6AZB-EemboE5CzRx_86Z_WQ2n0QXMw");

        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest
                        .get("/your-url")
                        .header("Authorization", "Bearer ".concat(jwt.getToken())));

        WebFilterChain filterChain = filterExchange -> Mono.empty();

        Mockito.when(jwtConverter.apply(Mockito.any())).thenReturn(Mono.just(jwt));
        Mockito.when(jwtService.findByJWT(Mockito.any())).thenReturn(Mono.just(jwt ));
        Mockito.when(interopService.getPrincipalFromPurposeId(Mockito.any()))
                        .thenReturn(Mono.just(BeanBuilder.getPrincipal()));
        Mockito.when(principalAgreementValidator.test(BeanBuilder.getPrincipal()))
                .thenReturn(true);

        Mockito.when(authSuccessHandler.onAuthenticationSuccess(Mockito.any(), Mockito.any()))
                        .thenReturn(Mono.just("").then());

        jwtFilter.filter(exchange,filterChain).block();

        ArgumentCaptor<Authentication> captureAuth = ArgumentCaptor.forClass(Authentication.class);


        Mockito.verify(reactiveAuthManager, Mockito.timeout(1000).times(1))
                .authenticate(captureAuth.capture());

        assertNotNull(captureAuth);
        assertNotNull(captureAuth.getValue());
        assertNotNull(captureAuth.getValue().getPrincipal());
        assertInstanceOf(PrincipalAgreement.class, captureAuth.getValue().getPrincipal());

    }


}