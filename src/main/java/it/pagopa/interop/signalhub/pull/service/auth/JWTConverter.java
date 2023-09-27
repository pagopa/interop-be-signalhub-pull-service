package it.pagopa.interop.signalhub.pull.service.auth;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.function.Function;

public class JWTConverter implements Function<ServerWebExchange, Mono<DecodedJWT>> {

    @Override
    public Mono<DecodedJWT> apply(ServerWebExchange serverWebExchange) {
        return Mono.justOrEmpty(serverWebExchange)
                .map(JWTUtil::getAuthorizationPayload)
                .filter(Objects::nonNull)
                .filter(JWTUtil.matchBearerLength())
                .map(JWTUtil.getBearerValue())
                .filter(token -> !token.isEmpty())
                .map(JWTUtil.decodeJwt())
                .filter(JWTUtil.matchType());
    }
}
