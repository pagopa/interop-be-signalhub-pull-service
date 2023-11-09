package it.pagopa.interop.signalhub.pull.service.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;

import java.util.function.Function;
import java.util.function.Predicate;

@Slf4j
public class JWTUtil {
    private static final String BEARER = "Bearer ";


    public static String getPurposeClaim(DecodedJWT jwt){
        return jwt.getClaim("purposeId").asString();
    }

    public static String getAuthorizationPayload(ServerWebExchange serverWebExchange) {
        return serverWebExchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);
    }

    public static Predicate<String> matchBearerLength(){
        return authValue -> authValue.length() > BEARER.length();
    }

    public static Function<String, String> getBearerValue(){
        return authValue -> authValue.substring(BEARER.length());
    }

    public static Function<String, DecodedJWT> decodeJwt() {
        return JWT::decode;
    }


}
