package it.pagopa.interop.signalhub.pull.service.cache.repository;


import it.pagopa.interop.signalhub.pull.service.cache.model.ConsumerEServiceCache;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;


@Slf4j
@Repository
@AllArgsConstructor
public class ConsumerEServiceCacheRepository {

    private final ReactiveRedisOperations<String, ConsumerEServiceCache> reactiveRedisOperations;

    public Mono<ConsumerEServiceCache> findById(String eserviceId, String consumerId) {
        return this.reactiveRedisOperations.opsForList()
                .indexOf(eserviceId.concat("-").concat(consumerId), getEserviceForSearching(eserviceId, consumerId))
                .flatMap(index -> this.reactiveRedisOperations.opsForList().index(eserviceId.concat("-").concat(consumerId), index));
    }


    public Mono<ConsumerEServiceCache> save(ConsumerEServiceCache consumerEServiceCache){
        return this.reactiveRedisOperations.opsForList()
                .leftPush(consumerEServiceCache.getEserviceId().concat("-").concat(consumerEServiceCache.getConsumerId()), consumerEServiceCache)
                .switchIfEmpty(Mono.defer(() -> {
                    log.info("[{}-{}] EService not saved on cache", consumerEServiceCache.getEserviceId(), consumerEServiceCache.getConsumerId());
                    return Mono.empty();
                }))
                .doOnNext(index -> log.info("[{}-{}] EService saved on cache", consumerEServiceCache.getEserviceId(), consumerEServiceCache.getConsumerId()))
                .thenReturn(consumerEServiceCache);
    }


    private ConsumerEServiceCache getEserviceForSearching(String eserviceId, String consumerId) {
        ConsumerEServiceCache cache = new ConsumerEServiceCache();
        cache.setEserviceId(eserviceId);
        cache.setConsumerId(consumerId);
        return cache;
    }


}
