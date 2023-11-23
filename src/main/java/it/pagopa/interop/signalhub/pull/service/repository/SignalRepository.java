package it.pagopa.interop.signalhub.pull.service.repository;

import it.pagopa.interop.signalhub.pull.service.entities.SignalEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface SignalRepository extends ReactiveCrudRepository<SignalEntity, Long> {

    @Query("select * from signal s where s.eservice_id= :eserviceId and s.signal_id BETWEEN :signalIdStart AND :signalIdEnd " +
            "order by s.signal_id")
    Flux<SignalEntity> findSignal(String eserviceId, Long signalIdStart, Long signalIdEnd);

    @Query("select max(s.signal_id) from signal s where s.eservice_id= :eserviceId " +
            "group by s.eservice_id")
    Mono<Integer> maxSignal(String eserviceId);

}


