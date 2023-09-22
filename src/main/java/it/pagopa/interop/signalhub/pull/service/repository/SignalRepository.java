package it.pagopa.interop.signalhub.pull.service.repository;

import it.pagopa.interop.signalhub.pull.service.rest.v1.dto.Signal;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface SignalRepository extends ReactiveCrudRepository<Signal, Long> {

    @Query("SELECT * FROM Signal WHERE eserviceId = :eserviceId ")
    Mono<Flux<Signal>> findSignal(String eserviceId, Long indexSignal);

}


