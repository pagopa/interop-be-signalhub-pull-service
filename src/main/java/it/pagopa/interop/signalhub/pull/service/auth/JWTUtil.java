package it.pagopa.interop.signalhub.pull.service.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import it.pagopa.interop.signalhub.pull.service.exception.JWTException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;

import java.util.function.Function;
import java.util.function.Predicate;

import static it.pagopa.interop.signalhub.pull.service.exception.ExceptionTypeEnum.JWT_PARSER_ERROR;

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
        return jwtString  -> {
            try {
                return JWT.decode(jwtString);
            } catch (JWTDecodeException ex) {
                throw new JWTException(JWT_PARSER_ERROR, JWT_PARSER_ERROR.getMessage(), HttpStatus.FORBIDDEN, jwtString);
            }
        };

    }


}
