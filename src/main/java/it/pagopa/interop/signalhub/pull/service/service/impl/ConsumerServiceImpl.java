package it.pagopa.interop.signalhub.pull.service.service.impl;

import it.pagopa.interop.signalhub.pull.service.entities.ConsumerEService;
import it.pagopa.interop.signalhub.pull.service.exception.PDNDGenericException;
import it.pagopa.interop.signalhub.pull.service.mapper.ConsumerEServiceMapper;
import it.pagopa.interop.signalhub.pull.service.repository.ConsumerEServiceRepository;
import it.pagopa.interop.signalhub.pull.service.cache.repository.ConsumerEServiceCacheRepository;
import it.pagopa.interop.signalhub.pull.service.service.ConsumerService;
import it.pagopa.interop.signalhub.pull.service.utils.Const;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static it.pagopa.interop.signalhub.pull.service.exception.ExceptionTypeEnum.*;
import static org.springframework.data.relational.core.query.Criteria.where;


@Slf4j
@Service
@AllArgsConstructor
public class ConsumerServiceImpl implements ConsumerService {

    private final ConsumerEServiceRepository consumerEServiceRepository;
    private final ConsumerEServiceCacheRepository cacheRepository;
    private final ConsumerEServiceMapper mapper;

    @Override
    public Mono<ConsumerEService> getConsumerEservice(String eserviceId, String consumerId ) {

        return this.cacheRepository.findById( eserviceId, consumerId )
                .doOnNext(cache -> log.info("[{}-{}] ConsumerEService in cache", consumerId, eserviceId))
                .flatMap(eServiceCache -> {
                    if(eServiceCache.getState().equalsIgnoreCase(Const.STATE_ACTIVE)) return Mono.just(eServiceCache);
                    return Mono.error(new PDNDGenericException(CONSUMER_STATUS_IS_NOT_ACTIVE, CONSUMER_STATUS_IS_NOT_ACTIVE.getMessage().concat(eserviceId)));
                })
                .switchIfEmpty(Mono.defer(() -> {
                    log.info("[{}-{}] ConsumerEService no in cache", consumerId, eserviceId);
                    return Mono.empty();
                }))
                .map(mapper::toEntity)
                .switchIfEmpty(getFromDbAndSaveOnCache(eserviceId, consumerId));
    }

    private Mono<ConsumerEService> getFromDbAndSaveOnCache(String eserviceId, String consumerId){

        return this.consumerEServiceRepository.findByConsumerIdAndEServiceIdAndState(eserviceId,consumerId, Const.STATE_ACTIVE)
                .switchIfEmpty(Mono.defer(()-> {
                    log.info("[{}-{}] ConsumerEService not founded into DB", consumerId, eserviceId);
                    return Mono.error(new PDNDGenericException(CONSUMER_ESERVICE_NOT_FOUND, CONSUMER_ESERVICE_NOT_FOUND.getMessage().concat(eserviceId)));
                }))
                .doOnNext(entity ->
                        log.info("[{}-{}] ConsumerEService founded into DB", consumerId, eserviceId)
                )
                .collectList()
                .map(list -> list.get(0))
                .flatMap(entity -> this.cacheRepository.save(mapper.toCache(entity)))
                .map(mapper::toEntity);
    }
}
