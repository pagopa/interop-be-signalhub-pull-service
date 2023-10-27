package it.pagopa.interop.signalhub.pull.service.repository.cache.repository;


import it.pagopa.interop.signalhub.pull.service.repository.cache.model.EServiceCache;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.function.Predicate;

@Slf4j
@Repository
@AllArgsConstructor
public class EServiceCacheRepository {
    private final ReactiveRedisOperations<String, EServiceCache> reactiveRedisOperations;

    public Mono<EServiceCache> findById(String eservice, String descriptor) {
        return this.reactiveRedisOperations.opsForList()
                .indexOf(eservice, getEserviceForSearching(eservice, descriptor))
                .flatMap(index -> this.reactiveRedisOperations.opsForList().index(eservice, index));
    }


    public Mono<EServiceCache> save(EServiceCache eservice){
        return this.reactiveRedisOperations.opsForList()
                .leftPush(eservice.getEserviceId(), eservice)
                .switchIfEmpty(Mono.defer(() -> {
                    log.info("[{}-{}] EService not saved on cache", eservice.getEserviceId(), eservice.getProducerId());
                    return Mono.empty();
                }))
                .doOnNext(index -> log.info("[{}-{}] EService saved on cache", eservice.getEserviceId(), eservice.getProducerId()))
                .thenReturn(eservice);
    }


    private EServiceCache getEserviceForSearching(String eserviceId, String descriptorId) {
        EServiceCache cache = new EServiceCache();
        cache.setEserviceId(eserviceId);
        cache.setDescriptorId(descriptorId);
        return cache;
    }

}
