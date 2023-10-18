package it.pagopa.interop.signalhub.pull.service.repository.cache.repository;


import it.pagopa.interop.signalhub.pull.service.repository.cache.model.JWTCache;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.function.Predicate;


@Repository
@AllArgsConstructor
public class JWTCacheRepository {
    private final ReactiveRedisOperations<String, JWTCache> reactiveRedisOperations;


    public Mono<JWTCache> findById(String jwt) {
        return this.reactiveRedisOperations.opsForList().range(jwt, 0, -1)
                .filter(correctJwt(jwt))
                .collectList()
                .flatMap(finded -> {
                    if (finded.isEmpty()) return Mono.empty();
                    return Mono.just(finded.get(finded.size()-1));
                });
    }


    public Mono<JWTCache> save(JWTCache jwt){
        return this.reactiveRedisOperations.opsForList().rightPush(jwt.getJwt(), jwt).thenReturn(jwt);
    }

    private Predicate<JWTCache> correctJwt(String jwt){
        return jwtCache -> jwtCache.getJwt().equals(jwt);
    }

}
