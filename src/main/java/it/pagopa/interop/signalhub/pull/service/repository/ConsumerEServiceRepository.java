package it.pagopa.interop.signalhub.pull.service.repository;

import it.pagopa.interop.signalhub.pull.service.entities.ConsumerEService;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ConsumerEServiceRepository extends ReactiveCrudRepository<ConsumerEService, String> {


    @Query("SELECT * FROM consumer_eservice c WHERE c.eservice_id = :eserviceId AND c.consumer_id = :consumerId AND UPPER(c.state) = UPPER(:state)")
    Mono<ConsumerEService> findByConsumerIdAndEServiceIdAndState(String eserviceId, String consumerId, String state);

}


