package it.pagopa.interop.signalhub.pull.service.auth;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import it.pagopa.interop.signalhub.pull.service.config.SignalHubPullConfig;
import it.pagopa.interop.signalhub.pull.service.exception.JWTException;
import it.pagopa.interop.signalhub.pull.service.exception.PDNDGenericException;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

import static it.pagopa.interop.signalhub.pull.service.exception.ExceptionTypeEnum.JWT_NOT_PRESENT;
import static it.pagopa.interop.signalhub.pull.service.exception.ExceptionTypeEnum.JWT_NOT_VALID;


@AllArgsConstructor
public class JWTConverter implements Function<ServerWebExchange, Mono<DecodedJWT>> {
    private static final String TYPE = "at+jwt";

    private final JwkProvider jwkProvider;
    private final SignalHubPullConfig signalHubPullConfig;


    @Override
    public Mono<DecodedJWT> apply(ServerWebExchange serverWebExchange) {
        return Mono.justOrEmpty(serverWebExchange)
                .map(JWTUtil::getAuthorizationPayload)
                .filter(Objects::nonNull)
                .switchIfEmpty(Mono.error(new PDNDGenericException(JWT_NOT_PRESENT, JWT_NOT_PRESENT.getMessage(), HttpStatus.UNAUTHORIZED)))
                .filter(JWTUtil.matchBearerLength())
                .switchIfEmpty(Mono.error(new PDNDGenericException(JWT_NOT_PRESENT, JWT_NOT_PRESENT.getMessage(), HttpStatus.UNAUTHORIZED)))
                .map(JWTUtil.getBearerValue())
                .filter(token -> !token.isEmpty())
                .switchIfEmpty(Mono.error(new PDNDGenericException(JWT_NOT_PRESENT, JWT_NOT_PRESENT.getMessage(), HttpStatus.UNAUTHORIZED)))
                .map(JWTUtil.decodeJwt())
                .filter(fieldsCheck())
                .map(validateToken())
                .switchIfEmpty(Mono.error(new PDNDGenericException(JWT_NOT_PRESENT, JWT_NOT_PRESENT.getMessage(), HttpStatus.UNAUTHORIZED)));
    }

    private Predicate<DecodedJWT> fieldsCheck(){
        return jwt -> {
            String claimPurpose = jwt.getClaim("purposeId").asString();
            String claimAud = jwt.getClaim("aud").asString();

            return (StringUtils.equals(jwt.getType(), TYPE) &&
                    StringUtils.isNotBlank(claimPurpose) &&
                    StringUtils.equals(claimAud, signalHubPullConfig.getAudience()));
        };
    }

    private Function<DecodedJWT, DecodedJWT> validateToken(){
        return jwt -> {
            PublicKey key = getPublicKey(jwt);
            JWTVerifier jwtVerifier = JWT.require(Algorithm.RSA256((RSAPublicKey) key, null))
                    .build();
            try {
                return jwtVerifier.verify(jwt);
            } catch (JWTVerificationException ex) {
                throw new JWTException(JWT_NOT_VALID, ex.getMessage(), HttpStatus.UNAUTHORIZED, jwt.getToken());
            }
        };
    }

    private PublicKey getPublicKey(DecodedJWT jwt) {
        try {
            Jwk jwk = jwkProvider.get(jwt.getKeyId());
            return jwk.getPublicKey();
        } catch (JwkException ex) {
            throw new JWTException(JWT_NOT_VALID, JWT_NOT_VALID.getMessage(), HttpStatus.UNAUTHORIZED, jwt.getToken());
        }
    }
}
