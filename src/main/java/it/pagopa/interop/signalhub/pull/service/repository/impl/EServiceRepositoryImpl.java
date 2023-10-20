package it.pagopa.interop.signalhub.pull.service.repository.impl;

import it.pagopa.interop.signalhub.pull.service.entities.EService;
import it.pagopa.interop.signalhub.pull.service.repository.EServiceRepository;
import it.pagopa.interop.signalhub.pull.service.utils.Const;
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
public class EServiceRepositoryImpl implements EServiceRepository {

    private final R2dbcEntityTemplate template;

    @Override
    public Mono<EService> checkEServiceStatus(String eserviceId) {
        Query equals = Query.query(
                where(EService.COLUMN_ESERVICE_ID).is(eserviceId)
                        .and(where(EService.COLUMN_STATE).not(Const.STATE_ARCHIVIED))
        );
        return this.template.selectOne(equals, EService.class)
                .switchIfEmpty(Mono.defer(()-> {
                    log.info("[{}] EService not founded into DB", eserviceId);
                    return Mono.empty();
                }))
                .doOnNext(entity ->
                        log.info("[{}] EService founded into DB", eserviceId));
    }

}
