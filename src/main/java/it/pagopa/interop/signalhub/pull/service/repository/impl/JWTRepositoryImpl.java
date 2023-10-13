package it.pagopa.interop.signalhub.pull.service.repository.impl;

import com.auth0.jwt.interfaces.DecodedJWT;
import it.pagopa.interop.signalhub.pull.service.exception.PDNDGenericException;
import it.pagopa.interop.signalhub.pull.service.repository.JWTRepository;
import it.pagopa.interop.signalhub.pull.service.repository.cache.model.JWTCache;
import it.pagopa.interop.signalhub.pull.service.repository.cache.repository.JWTCacheRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import static it.pagopa.interop.signalhub.pull.service.exception.ExceptionTypeEnum.JWT_NOT_VALID;


@Slf4j
@Repository
@AllArgsConstructor
public class JWTRepositoryImpl implements JWTRepository {

    private final JWTCacheRepository cacheRepository;

    @Override
    public Mono<DecodedJWT> findByJWT(DecodedJWT jwt) {

        return this.cacheRepository.findById(jwt.getToken())
                .doOnNext(cache -> log.info("[{}] JWT in cache", jwt.getToken()))
                .flatMap(jwtCache -> Mono.error(new PDNDGenericException(JWT_NOT_VALID, JWT_NOT_VALID.getMessage(), HttpStatus.UNAUTHORIZED)))
                .switchIfEmpty(Mono.defer(() -> {
                    log.info("[{}] JWT no in cache", jwt.getToken());
                    return Mono.just(jwt);
                })).thenReturn(jwt);

    }

    public Mono<JWTCache> saveOnCache(JWTCache jwtCache){
        return this.cacheRepository.save(jwtCache)
                .doOnNext(cacheEntity ->
                        log.info("[{}-{}] jwt saved on cache", jwtCache.getJwt())
                );
    }
}
