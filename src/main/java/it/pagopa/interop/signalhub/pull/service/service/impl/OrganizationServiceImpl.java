package it.pagopa.interop.signalhub.pull.service.service.impl;

import it.pagopa.interop.signalhub.pull.service.entities.EService;
import it.pagopa.interop.signalhub.pull.service.exception.PDNDGenericException;
import it.pagopa.interop.signalhub.pull.service.mapper.EServiceMapper;
import it.pagopa.interop.signalhub.pull.service.repository.EServiceRepository;
import it.pagopa.interop.signalhub.pull.service.cache.repository.EServiceCacheRepository;
import it.pagopa.interop.signalhub.pull.service.service.OrganizationService;
import it.pagopa.interop.signalhub.pull.service.utils.Const;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


import static it.pagopa.interop.signalhub.pull.service.exception.ExceptionTypeEnum.ESERVICE_NOT_FOUND;
import static it.pagopa.interop.signalhub.pull.service.exception.ExceptionTypeEnum.ESERVICE_STATUS_IS_NOT_PUBLISHED;
import static org.springframework.data.relational.core.query.Criteria.where;


@Slf4j
@Service
@AllArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

    private final EServiceRepository eServiceRepository;
    private final EServiceCacheRepository cacheRepository;
    private final EServiceMapper mapper;

    @Override
    public Mono<EService> getEService(String eserviceId, String descriptorId) {

        return this.cacheRepository.findById(eserviceId , descriptorId)
                .doOnNext(cache -> log.info("[{}-{}] EService in cache", eserviceId, descriptorId))
                .flatMap(eServiceCache -> {
                    if(eServiceCache.getState().equalsIgnoreCase(Const.STATE_PUBLISHED)) return Mono.just(eServiceCache);
                    return Mono.error(new PDNDGenericException(ESERVICE_STATUS_IS_NOT_PUBLISHED, ESERVICE_STATUS_IS_NOT_PUBLISHED.getMessage().concat(eserviceId)));
                })
                .switchIfEmpty(Mono.defer(() -> {
                    log.info("[{}-{}] EService no in cache",  eserviceId, descriptorId);
                    return Mono.empty();
                }))
                .map(mapper::toEntity)
                .switchIfEmpty(getFromDbAndSaveOnCache(eserviceId , descriptorId));

    }

    private Mono<EService> getFromDbAndSaveOnCache(String eserviceId, String descriptorId) {

        return this.eServiceRepository.checkEServiceStatus(eserviceId, descriptorId, Const.STATE_PUBLISHED)
                .switchIfEmpty(Mono.defer(()-> {
                    log.info("[{}-{}] EService not founded into DB",  eserviceId, descriptorId);
                    return Mono.error(new PDNDGenericException(ESERVICE_NOT_FOUND, ESERVICE_NOT_FOUND.getMessage().concat(eserviceId)));
                }))
                .doOnNext(entity ->
                        log.info("[{}-{}] EService founded into DB",  eserviceId, descriptorId)
                )
                .flatMap(entity -> this.cacheRepository.save(mapper.toCache(entity)))
                .map(mapper::toEntity);
    }

}
