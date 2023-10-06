package it.pagopa.interop.signalhub.pull.service.repository.cache.repository;


import it.pagopa.interop.signalhub.pull.service.repository.cache.model.ConsumerEServiceCache;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Predicate;


@Repository
@AllArgsConstructor
public class ConsumerEServiceCacheRepository {
    private final ReactiveRedisOperations<String, ConsumerEServiceCache> reactiveRedisOperations;

    private Flux<ConsumerEServiceCache> findAll(){
        return this.reactiveRedisOperations.opsForList().range("consumer_eservice", 0, -1);
    }

    public Mono<ConsumerEServiceCache> findById(String consumer, String eservice) {
        return this.findAll()
                .filter(correctEservice(consumer, eservice))
                .collectList()
                .flatMap(finded -> {
                    if (finded.isEmpty()) return Mono.empty();
                    return Mono.just(finded.get(finded.size()-1));
                });
    }


    public Mono<ConsumerEServiceCache> save(ConsumerEServiceCache eservice){
        return this.reactiveRedisOperations.opsForList().rightPush("consumer_eservice", eservice).thenReturn(eservice);
    }

    private Predicate<ConsumerEServiceCache> correctEservice(String consumerId, String eserviceId){
        return eservice -> eservice.getEserviceId().equals(eserviceId) &&
                                eservice.getConsumerId().equals(consumerId);
    }


}
