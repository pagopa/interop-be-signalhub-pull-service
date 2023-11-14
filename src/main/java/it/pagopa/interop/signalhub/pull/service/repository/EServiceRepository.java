package it.pagopa.interop.signalhub.pull.service.repository;

import it.pagopa.interop.signalhub.pull.service.entities.EService;
import it.pagopa.interop.signalhub.pull.service.entities.SignalEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface EServiceRepository extends ReactiveCrudRepository<EService, String> {

    @Query("SELECT * FROM eservice e WHERE e.eservice_id = :eserviceId AND e.descriptor_id = :descriptorId AND UPPER(e.state) = UPPER(:state)")
    Flux<EService> checkEServiceStatus(String eserviceId, String descriptorId, String state);

}


