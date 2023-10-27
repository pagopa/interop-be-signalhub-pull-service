package it.pagopa.interop.signalhub.pull.service.repository;

import it.pagopa.interop.signalhub.pull.service.entities.EService;
import reactor.core.publisher.Mono;


public interface EServiceRepository {

    Mono<EService> checkEServiceStatus(String eserviceId, String descriptorId);

}


