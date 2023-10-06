package it.pagopa.interop.signalhub.pull.service.repository.impl;

import it.pagopa.interop.signalhub.pull.service.entities.ConsumerEService;
import it.pagopa.interop.signalhub.pull.service.mapper.ConsumerEServiceMapper;
import it.pagopa.interop.signalhub.pull.service.repository.ConsumerEServiceRepository;
import it.pagopa.interop.signalhub.pull.service.repository.cache.repository.ConsumerEServiceCacheRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import static org.springframework.data.relational.core.query.Criteria.where;


@Slf4j
@Repository
@AllArgsConstructor
public class ConsumerEServiceRepositoryImpl implements ConsumerEServiceRepository {

    private final R2dbcEntityTemplate template;
    private final ConsumerEServiceCacheRepository cacheRepository;
    private final ConsumerEServiceMapper mapper;

    @Override
    public Mono<ConsumerEService> findByConsumerIdAndEServiceId(String consumerId, String eserviceId) {

        return this.cacheRepository.findById(consumerId, eserviceId)
                .doOnNext(cache -> log.info("[{}-{}] ConsumerEService in cache", consumerId, eserviceId))
                .switchIfEmpty(Mono.defer(() -> {
                    log.info("[{}-{}] ConsumerEService no in cache", consumerId, eserviceId);
                    return Mono.empty();
                }))
                .map(mapper::toEntity)
                .switchIfEmpty(getFromDbAndSaveOnCache(consumerId, eserviceId));

    }

    private Mono<ConsumerEService> getFromDbAndSaveOnCache(String consumerId, String eserviceId){
        Query equals = Query.query(
                where(ConsumerEService.COLUMN_CONSUMER_ID).is(consumerId)
                        .and(where(ConsumerEService.COLUMN_ESERVICE_ID).is(eserviceId))
        );
        return this.template.selectOne(equals, ConsumerEService.class)
                .switchIfEmpty(Mono.defer(()-> {
                    log.info("[{}-{}] ConsumerEService not founded into DB", consumerId, eserviceId);
                    return Mono.empty();
                }))
                .doOnNext(entity ->
                        log.info("[{}-{}] ConsumerEService founded into DB", consumerId, eserviceId)
                )
                .flatMap(entity -> this.cacheRepository.save(mapper.toCache(entity)))
                .doOnNext(cacheEntity ->
                        log.info("[{}-{}] ConsumerEService saved on cache", consumerId, eserviceId)
                )
                .map(mapper::toEntity);
    }
}
