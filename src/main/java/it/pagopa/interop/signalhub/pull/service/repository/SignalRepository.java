package it.pagopa.interop.signalhub.pull.service.repository;

import it.pagopa.interop.signalhub.pull.service.rest.v1.dto.Signal;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface SignalRepository extends ReactiveCrudRepository<Signal, Long> {

    @Query("select * from signal s where s.eservice_id= :eserviceId and s.signal_id BETWEEN indexSignal AND indexSignal " +
            "order by s.signal_id;")
    Mono<Flux<Signal>> findSignal(String eserviceId, Long indexSignalStart, Long indexSignalEnd);

}


