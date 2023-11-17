package it.pagopa.interop.signalhub.pull.service.repository.impl;

import it.pagopa.interop.signalhub.pull.service.entities.ConsumerEService;
import it.pagopa.interop.signalhub.pull.service.exception.PDNDGenericException;
import it.pagopa.interop.signalhub.pull.service.mapper.ConsumerEServiceMapper;
import it.pagopa.interop.signalhub.pull.service.repository.ConsumerEServiceRepository;
import it.pagopa.interop.signalhub.pull.service.repository.cache.repository.ConsumerEServiceCacheRepository;
import it.pagopa.interop.signalhub.pull.service.utils.Const;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import static it.pagopa.interop.signalhub.pull.service.exception.ExceptionTypeEnum.*;
import static org.springframework.data.relational.core.query.Criteria.where;


@Slf4j
@Repository
@AllArgsConstructor
public class ConsumerEServiceRepositoryImpl implements ConsumerEServiceRepository {

    private final R2dbcEntityTemplate template;
    private final ConsumerEServiceCacheRepository cacheRepository;
    private final ConsumerEServiceMapper mapper;

    @Override
    public Mono<ConsumerEService> findByConsumerIdAndEServiceId( String eserviceId, String consumerId ) {

        return this.cacheRepository.findById( eserviceId, consumerId )
                .doOnNext(cache -> log.info("[{}-{}] ConsumerEService in cache", consumerId, eserviceId))
                .flatMap(eServiceCache -> {
                    if(eServiceCache.getState().equalsIgnoreCase(Const.STATE_ACTIVE)) return Mono.just(eServiceCache);
                    return Mono.error(new PDNDGenericException(CONSUMER_STATUS_IS_NOT_ACTIVE, CONSUMER_STATUS_IS_NOT_ACTIVE.getMessage().concat(eserviceId), HttpStatus.FORBIDDEN));
                })
                .switchIfEmpty(Mono.defer(() -> {
                    log.info("[{}-{}] ConsumerEService no in cache", consumerId, eserviceId);
                    return Mono.empty();
                }))
                .map(mapper::toEntity)
                .switchIfEmpty(getFromDbAndSaveOnCache(eserviceId, consumerId));
    }

    private Mono<ConsumerEService> getFromDbAndSaveOnCache(String eserviceId, String consumerId){
        Query equals = Query.query(
                where(ConsumerEService.COLUMN_CONSUMER_ID).is(consumerId)
                        .and(where(ConsumerEService.COLUMN_ESERVICE_ID).is(eserviceId))
                        .and(where(ConsumerEService.COLUMN_STATE).is(Const.STATE_ACTIVE).ignoreCase(true))
        );
        return this.template.selectOne(equals, ConsumerEService.class)
                .switchIfEmpty(Mono.defer(()-> {
                    log.info("[{}-{}] ConsumerEService not founded into DB", consumerId, eserviceId);
                    return Mono.error(new PDNDGenericException(CONSUMER_ESERVICE_NOT_FOUND, CONSUMER_ESERVICE_NOT_FOUND.getMessage().concat(eserviceId), HttpStatus.FORBIDDEN));
                }))
                .doOnNext(entity ->
                        log.info("[{}-{}] ConsumerEService founded into DB", consumerId, eserviceId)
                )
                .flatMap(entity -> this.cacheRepository.save(mapper.toCache(entity)))
                .map(mapper::toEntity);
    }
}
