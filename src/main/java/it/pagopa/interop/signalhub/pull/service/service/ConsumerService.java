package it.pagopa.interop.signalhub.pull.service.service;

import it.pagopa.interop.signalhub.pull.service.entities.ConsumerEService;
import reactor.core.publisher.Mono;

public interface ConsumerService {

    Mono<ConsumerEService> getConsumerEservice(String eserviceId, String consumerId);

}


