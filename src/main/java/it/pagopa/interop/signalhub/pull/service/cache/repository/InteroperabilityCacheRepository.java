package it.pagopa.interop.signalhub.pull.service.cache.repository;


import it.pagopa.interop.signalhub.pull.service.generated.openapi.client.interop.model.v1.Agreement;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;


@Slf4j
@Repository
@AllArgsConstructor
public class InteroperabilityCacheRepository {
    private final ReactiveRedisOperations<String, Agreement> reactiveRedisOperations;

    public Mono<Agreement> findById(String purposeId) {
        return this.reactiveRedisOperations.opsForValue().get(purposeId)
                .flatMap(Mono::just);
    }

    public Mono<Agreement> save(Agreement agreement, String purposeId){
        return this.reactiveRedisOperations.opsForValue()
                .set(purposeId, agreement)
                .thenReturn(agreement);
    }


}
