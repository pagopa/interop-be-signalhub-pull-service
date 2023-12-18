package it.pagopa.interop.signalhub.pull.service.auth;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.Verification;
import it.pagopa.interop.signalhub.pull.service.config.BeanBuilder;
import it.pagopa.interop.signalhub.pull.service.config.SignalHubPullConfig;
import it.pagopa.interop.signalhub.pull.service.exception.JWTException;
import it.pagopa.interop.signalhub.pull.service.exception.PDNDGenericException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import reactor.test.StepVerifier;

import java.security.NoSuchAlgorithmException;
import java.util.function.Function;
import java.util.function.Predicate;


@ExtendWith(MockitoExtension.class)
class JWTConverterTest {
    @InjectMocks
    private JWTConverter jwtConverter;
    @Mock
    private JwkProvider jwkProvider;
    @Mock
    private SignalHubPullConfig signalHubPullConfig;


    @Test
    void applyWhenJwtIsCorrectTest() throws JwkException, NoSuchAlgorithmException {
        DecodedJWT jwt = JWT.decode("eyJ0eXAiOiJhdCtqd3QiLCJhbGciOiJSUzI1NiIsInVzZSI6InNpZyIsImtpZCI6IjMyZDhhMzIxLTE1NjgtNDRmNS05NTU4LWE5MDcyZjUxOWQyZCJ9.eyJhdWQiOiJpbnRlcm9wLXNpZ25hbGh1Yi1wdWxsLXNpZ25hbCIsInN1YiI6ImY3YzFhZDIwLWIwZDktNDIxMi1iMGIwLTQ2NTkyODgzNTY2MyIsIm5iZiI6MTcwMTQyNDM4OCwicHVycG9zZUlkIjoiOTI3YmM2Y2UtNjU5NS00ODU4LTkyMmItODgyMGU3MzgwZjZhIiwiaXNzIjoidWF0LmludGVyb3AucGFnb3BhLml0IiwiZXhwIjoxNzAxNDI2MTg4LCJpYXQiOjE3MDE0MjQzODgsImNsaWVudF9pZCI6ImY3YzFhZDIwLWIwZDktNDIxMi1iMGIwLTQ2NTkyODgzNTY2MyIsImp0aSI6IjRlNjQxNzlkLTRiOWEtNDVjZS1hYTBjLWYxMzVkNzZkNGJkNCJ9.gSTG_JuDVLgaPaXmc0FEGUTy2x5TBc86lOTcvmsl95aagWcr2UEfKy_0Q0I4BDIsFMbYKiy8B6igJryMXg43WMbPx8aXNQOkUWqzlVpPA8yTYYeppiLuMLr8HHv3yDbRoAEq-CRmyOLyNN0s_yCSk1UpqCMxJCo6nhMnAN-UNZYjCsWatMxMIzwrNRiEUNORBgVw6sVo9djFQ1f3ejUyM-2M2lCv4OxudzdHKuVmQGBPNU7kVAsfakHbbJhWxgPW8sqbIiwaUoq4JNF0dgFdSxjzW5CZuwv8cDUsNNskq4o1dkhZnWQZQYDQ7yH8RtjanejgWSykycnw6xxZxyuZ7g");

        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest
                        .get("/your-url")
                        .header("Authorization", "Bearer ".concat(jwt.getToken())));

        String audience = "interop-signalhub-pull-signal";
        Mockito.when(signalHubPullConfig.getAudience())
                        .thenReturn(audience);

        MockedStatic<JWT> jwtMockedStatic = Mockito.mockStatic(JWT.class);
        jwtMockedStatic
                .when(() -> JWT.decode(Mockito.any()))
                .thenReturn(jwt);

        Jwk jwk = Mockito.mock(Jwk.class);
        Mockito.when(jwkProvider.get(Mockito.any()))
                .thenReturn(jwk);

        Mockito.when(jwk.getPublicKey())
                        .thenReturn(BeanBuilder.getPublicKey());

        Algorithm algorithm = Mockito.mock(Algorithm.class);

        MockedStatic<Algorithm> algorithmMockedStatic = Mockito.mockStatic(Algorithm.class);
        algorithmMockedStatic
                .when(() -> Algorithm.RSA256(Mockito.any(), Mockito.any()))
                .thenReturn(algorithm);

        Verification verification = Mockito.mock(Verification.class);
        jwtMockedStatic
                .when(() -> JWT.require(Mockito.any()))
                .thenReturn(verification);

        JWTVerifier jwtVerifier = Mockito.mock(JWTVerifier.class);
        Mockito.when(verification.build())
                .thenReturn(jwtVerifier);

        Mockito.when(jwtVerifier.verify(jwt))
                .thenReturn(jwt);

        DecodedJWT djwt = jwtConverter.apply(exchange)
                .block();
        Assertions.assertNotNull(djwt);
        Assertions.assertEquals(jwt, djwt);

        algorithmMockedStatic.close();
        jwtMockedStatic.close();
    }

    @Test
    void applyWhenJwtIsEmptyTest() {
        DecodedJWT jwt = JWT.decode("eyJ0eXAiOiJhdCtqd3QiLCJhbGciOiJSUzI1NiIsInVzZSI6InNpZyIsImtpZCI6IjMyZDhhMzIxLTE1NjgtNDRmNS05NTU4LWE5MDcyZjUxOWQyZCJ9.eyJhdWQiOiJpbnRlcm9wLXNpZ25hbGh1Yi1wdWxsLXNpZ25hbCIsInN1YiI6ImY3YzFhZDIwLWIwZDktNDIxMi1iMGIwLTQ2NTkyODgzNTY2MyIsIm5iZiI6MTcwMTQyNDM4OCwicHVycG9zZUlkIjoiOTI3YmM2Y2UtNjU5NS00ODU4LTkyMmItODgyMGU3MzgwZjZhIiwiaXNzIjoidWF0LmludGVyb3AucGFnb3BhLml0IiwiZXhwIjoxNzAxNDI2MTg4LCJpYXQiOjE3MDE0MjQzODgsImNsaWVudF9pZCI6ImY3YzFhZDIwLWIwZDktNDIxMi1iMGIwLTQ2NTkyODgzNTY2MyIsImp0aSI6IjRlNjQxNzlkLTRiOWEtNDVjZS1hYTBjLWYxMzVkNzZkNGJkNCJ9.gSTG_JuDVLgaPaXmc0FEGUTy2x5TBc86lOTcvmsl95aagWcr2UEfKy_0Q0I4BDIsFMbYKiy8B6igJryMXg43WMbPx8aXNQOkUWqzlVpPA8yTYYeppiLuMLr8HHv3yDbRoAEq-CRmyOLyNN0s_yCSk1UpqCMxJCo6nhMnAN-UNZYjCsWatMxMIzwrNRiEUNORBgVw6sVo9djFQ1f3ejUyM-2M2lCv4OxudzdHKuVmQGBPNU7kVAsfakHbbJhWxgPW8sqbIiwaUoq4JNF0dgFdSxjzW5CZuwv8cDUsNNskq4o1dkhZnWQZQYDQ7yH8RtjanejgWSykycnw6xxZxyuZ7g");

        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest
                        .get("/your-url")
                        .header("Authorization", "Bearer ".concat(jwt.getToken())));

        MockedStatic<JWTUtil> jwtUtilMockedStatic = Mockito.mockStatic(JWTUtil.class);
        jwtUtilMockedStatic
                .when(() -> JWTUtil.getAuthorizationPayload(Mockito.any()))
                .thenReturn("Bearer ".concat(""));

        Predicate<String> predicate = pred -> true;
        jwtUtilMockedStatic
                .when(() -> JWTUtil.matchBearerLength())
                .thenReturn(predicate);

        Function<String, String> function = f -> "";
        jwtUtilMockedStatic
                .when(() -> JWTUtil.getBearerValue())
                .thenReturn(function);

        Function<String, DecodedJWT> toDecode = val -> null;
        jwtUtilMockedStatic
                .when(() -> JWTUtil.decodeJwt())
                .thenReturn(toDecode);

        StepVerifier.create(jwtConverter.apply(exchange))
                .expectError(PDNDGenericException.class)
                .verify();

        jwtUtilMockedStatic.close();
    }

    @Test
    void applyWhenJwtIsNotPresentTest() {
        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest
                        .get("/your-url")
                        .header("Authorization", "Bearer "));
        StepVerifier.create(jwtConverter.apply(exchange))
                .expectError(PDNDGenericException.class)
                .verify();

        exchange = MockServerWebExchange.from(
                MockServerHttpRequest
                        .get("/your-url")
                        .header("Authorization", ""));
        StepVerifier.create(jwtConverter.apply(exchange))
                .expectError(PDNDGenericException.class)
                .verify();

        exchange = MockServerWebExchange.from(
                MockServerHttpRequest
                        .get("/your-url")
                        .header("Authorization", "Bearer ".concat("")));
        StepVerifier.create(jwtConverter.apply(exchange))
                .expectError(PDNDGenericException.class)
                .verify();
    }

    @Test
    void applyWhenOnDecodeJWTThrowJWTExceptionTest() {
        DecodedJWT jwt = JWT.decode("eyJ0eXAiOiJhdCtqd3QiLCJhbGciOiJSUzI1NiIsInVzZSI6InNpZyIsImtpZCI6IjMyZDhhMzIxLTE1NjgtNDRmNS05NTU4LWE5MDcyZjUxOWQyZCJ9.eyJhdWQiOiJpbnRlcm9wLXNpZ25hbGh1Yi1wdWxsLXNpZ25hbCIsInN1YiI6ImY3YzFhZDIwLWIwZDktNDIxMi1iMGIwLTQ2NTkyODgzNTY2MyIsIm5iZiI6MTcwMTQyNDM4OCwicHVycG9zZUlkIjoiOTI3YmM2Y2UtNjU5NS00ODU4LTkyMmItODgyMGU3MzgwZjZhIiwiaXNzIjoidWF0LmludGVyb3AucGFnb3BhLml0IiwiZXhwIjoxNzAxNDI2MTg4LCJpYXQiOjE3MDE0MjQzODgsImNsaWVudF9pZCI6ImY3YzFhZDIwLWIwZDktNDIxMi1iMGIwLTQ2NTkyODgzNTY2MyIsImp0aSI6IjRlNjQxNzlkLTRiOWEtNDVjZS1hYTBjLWYxMzVkNzZkNGJkNCJ9.gSTG_JuDVLgaPaXmc0FEGUTy2x5TBc86lOTcvmsl95aagWcr2UEfKy_0Q0I4BDIsFMbYKiy8B6igJryMXg43WMbPx8aXNQOkUWqzlVpPA8yTYYeppiLuMLr8HHv3yDbRoAEq-CRmyOLyNN0s_yCSk1UpqCMxJCo6nhMnAN-UNZYjCsWatMxMIzwrNRiEUNORBgVw6sVo9djFQ1f3ejUyM-2M2lCv4OxudzdHKuVmQGBPNU7kVAsfakHbbJhWxgPW8sqbIiwaUoq4JNF0dgFdSxjzW5CZuwv8cDUsNNskq4o1dkhZnWQZQYDQ7yH8RtjanejgWSykycnw6xxZxyuZ7g");

        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest
                        .get("/your-url")
                        .header("Authorization", "Bearer ".concat(jwt.getToken())));

        MockedStatic<JWT> jwtMockedStatic = Mockito.mockStatic(JWT.class);
        jwtMockedStatic
                .when(() -> JWT.decode(Mockito.any()))
                .thenThrow(new JWTDecodeException(""));

        StepVerifier.create(jwtConverter.apply(exchange))
                .expectError(JWTException.class)
                .verify();

        jwtMockedStatic.close();
    }

    @Test
    void applyWhenOnVerifyJWTThrowJWTExceptionTest() throws JwkException, NoSuchAlgorithmException {
        DecodedJWT jwt = JWT.decode("eyJ0eXAiOiJhdCtqd3QiLCJhbGciOiJSUzI1NiIsInVzZSI6InNpZyIsImtpZCI6IjMyZDhhMzIxLTE1NjgtNDRmNS05NTU4LWE5MDcyZjUxOWQyZCJ9.eyJhdWQiOiJpbnRlcm9wLXNpZ25hbGh1Yi1wdWxsLXNpZ25hbCIsInN1YiI6ImY3YzFhZDIwLWIwZDktNDIxMi1iMGIwLTQ2NTkyODgzNTY2MyIsIm5iZiI6MTcwMTQyNDM4OCwicHVycG9zZUlkIjoiOTI3YmM2Y2UtNjU5NS00ODU4LTkyMmItODgyMGU3MzgwZjZhIiwiaXNzIjoidWF0LmludGVyb3AucGFnb3BhLml0IiwiZXhwIjoxNzAxNDI2MTg4LCJpYXQiOjE3MDE0MjQzODgsImNsaWVudF9pZCI6ImY3YzFhZDIwLWIwZDktNDIxMi1iMGIwLTQ2NTkyODgzNTY2MyIsImp0aSI6IjRlNjQxNzlkLTRiOWEtNDVjZS1hYTBjLWYxMzVkNzZkNGJkNCJ9.gSTG_JuDVLgaPaXmc0FEGUTy2x5TBc86lOTcvmsl95aagWcr2UEfKy_0Q0I4BDIsFMbYKiy8B6igJryMXg43WMbPx8aXNQOkUWqzlVpPA8yTYYeppiLuMLr8HHv3yDbRoAEq-CRmyOLyNN0s_yCSk1UpqCMxJCo6nhMnAN-UNZYjCsWatMxMIzwrNRiEUNORBgVw6sVo9djFQ1f3ejUyM-2M2lCv4OxudzdHKuVmQGBPNU7kVAsfakHbbJhWxgPW8sqbIiwaUoq4JNF0dgFdSxjzW5CZuwv8cDUsNNskq4o1dkhZnWQZQYDQ7yH8RtjanejgWSykycnw6xxZxyuZ7g");

        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest
                        .get("/your-url")
                        .header("Authorization", "Bearer ".concat(jwt.getToken())));

        String audience = "interop-signalhub-pull-signal";
        Mockito.when(signalHubPullConfig.getAudience())
                .thenReturn(audience);

        MockedStatic<JWT> jwtMockedStatic = Mockito.mockStatic(JWT.class);
        jwtMockedStatic
                .when(() -> JWT.decode(Mockito.any()))
                .thenReturn(jwt);

        Jwk jwk = Mockito.mock(Jwk.class);
        Mockito.when(jwkProvider.get(Mockito.any()))
                .thenReturn(jwk);

        Mockito.when(jwk.getPublicKey())
                .thenReturn(BeanBuilder.getPublicKey());

        Algorithm algorithm = Mockito.mock(Algorithm.class);

        MockedStatic<Algorithm> algorithmMockedStatic = Mockito.mockStatic(Algorithm.class);
        algorithmMockedStatic
                .when(() -> Algorithm.RSA256(Mockito.any(), Mockito.any()))
                .thenReturn(algorithm);

        Verification verification = Mockito.mock(Verification.class);
        jwtMockedStatic
                .when(() -> JWT.require(Mockito.any()))
                .thenReturn(verification);

        JWTVerifier jwtVerifier = Mockito.mock(JWTVerifier.class);
        Mockito.when(verification.build())
                .thenReturn(jwtVerifier);

        Mockito.when(jwtVerifier.verify(jwt))
                .thenThrow(new JWTVerificationException(""));

        StepVerifier.create(jwtConverter.apply(exchange))
                .expectError(JWTException.class)
                .verify();

        algorithmMockedStatic.close();
        jwtMockedStatic.close();
    }

    @Test
    void applyWhenOnGetPublicKeyThrowJWTExceptionTest() throws JwkException, NoSuchAlgorithmException {
        DecodedJWT jwt = JWT.decode("eyJ0eXAiOiJhdCtqd3QiLCJhbGciOiJSUzI1NiIsInVzZSI6InNpZyIsImtpZCI6IjMyZDhhMzIxLTE1NjgtNDRmNS05NTU4LWE5MDcyZjUxOWQyZCJ9.eyJhdWQiOiJpbnRlcm9wLXNpZ25hbGh1Yi1wdWxsLXNpZ25hbCIsInN1YiI6ImY3YzFhZDIwLWIwZDktNDIxMi1iMGIwLTQ2NTkyODgzNTY2MyIsIm5iZiI6MTcwMTQyNDM4OCwicHVycG9zZUlkIjoiOTI3YmM2Y2UtNjU5NS00ODU4LTkyMmItODgyMGU3MzgwZjZhIiwiaXNzIjoidWF0LmludGVyb3AucGFnb3BhLml0IiwiZXhwIjoxNzAxNDI2MTg4LCJpYXQiOjE3MDE0MjQzODgsImNsaWVudF9pZCI6ImY3YzFhZDIwLWIwZDktNDIxMi1iMGIwLTQ2NTkyODgzNTY2MyIsImp0aSI6IjRlNjQxNzlkLTRiOWEtNDVjZS1hYTBjLWYxMzVkNzZkNGJkNCJ9.gSTG_JuDVLgaPaXmc0FEGUTy2x5TBc86lOTcvmsl95aagWcr2UEfKy_0Q0I4BDIsFMbYKiy8B6igJryMXg43WMbPx8aXNQOkUWqzlVpPA8yTYYeppiLuMLr8HHv3yDbRoAEq-CRmyOLyNN0s_yCSk1UpqCMxJCo6nhMnAN-UNZYjCsWatMxMIzwrNRiEUNORBgVw6sVo9djFQ1f3ejUyM-2M2lCv4OxudzdHKuVmQGBPNU7kVAsfakHbbJhWxgPW8sqbIiwaUoq4JNF0dgFdSxjzW5CZuwv8cDUsNNskq4o1dkhZnWQZQYDQ7yH8RtjanejgWSykycnw6xxZxyuZ7g");

        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest
                        .get("/your-url")
                        .header("Authorization", "Bearer ".concat(jwt.getToken())));

        String audience = "interop-signalhub-pull-signal";
        Mockito.when(signalHubPullConfig.getAudience())
                .thenReturn(audience);

        MockedStatic<JWT> jwtMockedStatic = Mockito.mockStatic(JWT.class);
        jwtMockedStatic
                .when(() -> JWT.decode(Mockito.any()))
                .thenReturn(jwt);

        Mockito.when(jwkProvider.get(Mockito.any()))
                .thenThrow(new JwkException(""));

        StepVerifier.create(jwtConverter.apply(exchange))
                .expectError(JWTException.class)
                .verify();

        jwtMockedStatic.close();
    }

    @Test
    void applyWhenJwtIsNotValidTest() {
        DecodedJWT jwt = JWT.decode("eyJ0eXAiOiJhdCtqd3QiLCJhbGciOiJSUzI1NiIsInVzZSI6InNpZyIsImtpZCI6IjMyZDhhMzIxLTE1NjgtNDRmNS05NTU4LWE5MDcyZjUxOWQyZCJ9.eyJhdWQiOiJpbnRlcm9wLXNpZ25hbGh1Yi1wdWxsLXNpZ25hbCIsInN1YiI6ImY3YzFhZDIwLWIwZDktNDIxMi1iMGIwLTQ2NTkyODgzNTY2MyIsIm5iZiI6MTcwMTQyNDM4OCwicHVycG9zZUlkIjoiOTI3YmM2Y2UtNjU5NS00ODU4LTkyMmItODgyMGU3MzgwZjZhIiwiaXNzIjoidWF0LmludGVyb3AucGFnb3BhLml0IiwiZXhwIjoxNzAxNDI2MTg4LCJpYXQiOjE3MDE0MjQzODgsImNsaWVudF9pZCI6ImY3YzFhZDIwLWIwZDktNDIxMi1iMGIwLTQ2NTkyODgzNTY2MyIsImp0aSI6IjRlNjQxNzlkLTRiOWEtNDVjZS1hYTBjLWYxMzVkNzZkNGJkNCJ9.gSTG_JuDVLgaPaXmc0FEGUTy2x5TBc86lOTcvmsl95aagWcr2UEfKy_0Q0I4BDIsFMbYKiy8B6igJryMXg43WMbPx8aXNQOkUWqzlVpPA8yTYYeppiLuMLr8HHv3yDbRoAEq-CRmyOLyNN0s_yCSk1UpqCMxJCo6nhMnAN-UNZYjCsWatMxMIzwrNRiEUNORBgVw6sVo9djFQ1f3ejUyM-2M2lCv4OxudzdHKuVmQGBPNU7kVAsfakHbbJhWxgPW8sqbIiwaUoq4JNF0dgFdSxjzW5CZuwv8cDUsNNskq4o1dkhZnWQZQYDQ7yH8RtjanejgWSykycnw6xxZxyuZ7g");

        MockServerWebExchange exchange = MockServerWebExchange.from(
                MockServerHttpRequest
                        .get("/your-url")
                        .header("Authorization", "Bearer ".concat(jwt.getToken())));

        StepVerifier.create(jwtConverter.apply(exchange))
                .expectError(PDNDGenericException.class)
                .verify();
    }
}