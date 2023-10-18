package it.pagopa.interop.signalhub.pull.service.repository.cache.repository;


import it.pagopa.interop.signalhub.pull.service.repository.cache.model.ConsumerEServiceCache;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.function.Predicate;


@Repository
@AllArgsConstructor
public class ConsumerEServiceCacheRepository {
    private final ReactiveRedisOperations<String, ConsumerEServiceCache> reactiveRedisOperations;


    public Mono<ConsumerEServiceCache> findById(String consumer, String eservice) {
        return this.reactiveRedisOperations.opsForList().range(eservice.concat("-").concat(consumer), 0, -1)
                .filter(correctEservice(consumer, eservice))
                .collectList()
                .flatMap(finded -> {
                    if (finded.isEmpty()) return Mono.empty();
                    return Mono.just(finded.get(finded.size()-1));
                });
    }


    public Mono<ConsumerEServiceCache> save(ConsumerEServiceCache consumerEServiceCache){
        return this.reactiveRedisOperations.opsForList().rightPush(consumerEServiceCache.getEserviceId().concat("-").concat(consumerEServiceCache.getConsumerId()), consumerEServiceCache).thenReturn(consumerEServiceCache);
    }

    private Predicate<ConsumerEServiceCache> correctEservice(String consumerId, String eserviceId){
        return consumerEServiceCache -> consumerEServiceCache.getEserviceId().equals(eserviceId) &&
                consumerEServiceCache.getConsumerId().equals(consumerId);
    }


}
