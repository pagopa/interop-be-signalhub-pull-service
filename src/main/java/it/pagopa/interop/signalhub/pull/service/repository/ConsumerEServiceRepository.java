package it.pagopa.interop.signalhub.pull.service.repository;

import it.pagopa.interop.signalhub.pull.service.entities.ConsumerEService;
import reactor.core.publisher.Mono;

public interface ConsumerEServiceRepository {

    Mono<ConsumerEService> findByConsumerIdAndEServiceId(String eserviceId, String consumerId);

}


