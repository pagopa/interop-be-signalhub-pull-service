package it.pagopa.interop.signalhub.pull.service.repository;

import it.pagopa.interop.signalhub.pull.service.entities.ConsumerEService;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ConsumerEserviceRepository extends ReactiveCrudRepository<ConsumerEService, Long> {

    @Query("SELECT * FROM CONSUMER_ESERVICE cs WHERE cs.eservice_id = :eserviceId AND cs.consumer_id = :consumerId")
    Mono<ConsumerEService> findByConsumerIdAndEServiceId(String consumerId, String eserviceId);

}


