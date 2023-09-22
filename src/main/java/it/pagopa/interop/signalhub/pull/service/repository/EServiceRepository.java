package it.pagopa.interop.signalhub.pull.service.repository;

import it.pagopa.interop.signalhub.pull.service.entities.EService;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface EServiceRepository extends ReactiveCrudRepository<EService, Long> {

    @Query("SELECT * FROM EService WHERE eserviceId = :eserviceId AND organizationId = :organizationId")
    Mono<EService> findByOrganizationIdAndEServiceId(String organizationId, String eserviceId);

}


