package it.pagopa.interop.signalhub.pull.service.service;

import it.pagopa.interop.signalhub.pull.service.entities.EService;
import reactor.core.publisher.Mono;


public interface OrganizationService {

    Mono<EService> getEService(String eserviceId, String descriptorId);

}


