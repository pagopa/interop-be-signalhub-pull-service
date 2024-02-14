package it.pagopa.interop.signalhub.pull.service.service;

import it.pagopa.interop.signalhub.pull.service.auth.PrincipalAgreement;
import reactor.core.publisher.Mono;

public interface InteropService {


    Mono<PrincipalAgreement> getPrincipalFromPurposeId(String purposeId);

}
