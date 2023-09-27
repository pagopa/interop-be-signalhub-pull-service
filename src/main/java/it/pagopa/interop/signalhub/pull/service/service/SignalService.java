package it.pagopa.interop.signalhub.pull.service.service;

import it.pagopa.interop.signalhub.pull.service.rest.v1.dto.Signal;
import reactor.core.publisher.Flux;

public interface SignalService {

    Flux<Signal> pullSignal(String organizationId, String eServiceId, Long indexSignal );

}
