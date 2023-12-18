package it.pagopa.interop.signalhub.pull.service.service;

import it.pagopa.interop.signalhub.pull.service.rest.v1.dto.Signal;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface SignalService {

    Flux<Signal> pullSignal(String consumerId, String eServiceId, Long SignalId, Long size );

    Mono<Integer> counter(String eServiceId);


}
