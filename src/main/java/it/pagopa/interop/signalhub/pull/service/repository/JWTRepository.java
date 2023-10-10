package it.pagopa.interop.signalhub.pull.service.repository;

import com.auth0.jwt.interfaces.DecodedJWT;
import it.pagopa.interop.signalhub.pull.service.repository.cache.model.JWTCache;
import reactor.core.publisher.Mono;


public interface JWTRepository {


    Mono<DecodedJWT> findByJWT(DecodedJWT jwt);
    Mono<JWTCache> saveOnCache(JWTCache jwt);

}


