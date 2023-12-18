package it.pagopa.interop.signalhub.pull.service.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import it.pagopa.interop.signalhub.pull.service.cache.model.JWTCache;
import reactor.core.publisher.Mono;


public interface JWTService {


    Mono<DecodedJWT> findByJWT(DecodedJWT jwt);
    Mono<JWTCache> saveOnCache(JWTCache jwt);

}


