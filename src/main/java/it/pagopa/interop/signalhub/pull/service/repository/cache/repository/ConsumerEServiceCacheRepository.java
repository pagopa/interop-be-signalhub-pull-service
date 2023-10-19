package it.pagopa.interop.signalhub.pull.service.repository.cache.repository;


import it.pagopa.interop.signalhub.pull.service.repository.cache.model.ConsumerEServiceCache;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;



@Repository
@AllArgsConstructor
public class ConsumerEServiceCacheRepository {
    private final ReactiveRedisOperations<String, ConsumerEServiceCache> reactiveRedisOperations;


    public Mono<ConsumerEServiceCache> findById(String consumer, String eservice) {
        return this.reactiveRedisOperations.opsForValue().get(eservice.concat("-").concat(consumer));
    }


    public Mono<ConsumerEServiceCache> save(ConsumerEServiceCache consumerEServiceCache){
        return this.reactiveRedisOperations.opsForValue()
                .set(consumerEServiceCache.getEserviceId().concat("-").concat(consumerEServiceCache.getConsumerId()), consumerEServiceCache)
                .thenReturn(consumerEServiceCache);
    }


}
