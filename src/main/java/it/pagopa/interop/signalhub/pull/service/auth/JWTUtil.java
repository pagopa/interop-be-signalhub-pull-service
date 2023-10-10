package it.pagopa.interop.signalhub.pull.service.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.net.HttpHeaders;
import it.pagopa.interop.signalhub.pull.service.exception.JWTException;
import it.pagopa.interop.signalhub.pull.service.exception.PocGenericException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.server.ServerWebExchange;

import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static it.pagopa.interop.signalhub.pull.service.exception.ExceptionTypeEnum.JWT_NOT_VALID;


public class JWTUtil {
    private static final String TYPE = "at+jwt";
    private static final String BEARER = "Bearer ";

    private JWTUtil(){}

    public static String getAuthorizationPayload(ServerWebExchange serverWebExchange) {
        return serverWebExchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);
    }

    public static Predicate<String> matchBearerLength(){
        return authValue -> authValue.length() > BEARER.length();
    }

    public static Predicate<DecodedJWT> matchType(){
        return jwt -> (StringUtils.equals(jwt.getType(), TYPE));
    }

    public static Function<String, String> getBearerValue(){
        return authValue -> authValue.substring(BEARER.length());
    }

    public static Function<String, DecodedJWT> decodeJwt() {
        return JWT::decode;
    }

    public static Function<DecodedJWT, DecodedJWT> verifyToken(Function<DecodedJWT, PublicKey> publicKeyFunction){
        return jwt -> {
            PublicKey key = publicKeyFunction.apply(jwt);
            JWTVerifier jwtVerifier = JWT.require(Algorithm.RSA256((RSAPublicKey) key, null))
                    .build();
            try {
                return jwtVerifier.verify(jwt);
            } catch (JWTVerificationException ex) {
                throw new JWTException(JWT_NOT_VALID, ex.getMessage(), HttpStatus.UNAUTHORIZED, jwt.getToken());
            }
        };
    }
    public static Authentication getAuthenticationJwt(DecodedJWT jwt) {
        String organizationId = jwt.getClaim("organizationId").asString();
        return new UsernamePasswordAuthenticationToken(organizationId, null, List.of(new SimpleGrantedAuthority("CONSUMER")));
    }

}
