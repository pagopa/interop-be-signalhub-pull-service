package it.pagopa.interop.signalhub.pull.service.externalclient.impl;


import it.pagopa.interop.signalhub.pull.service.externalclient.InteroperabilityClient;
import it.pagopa.interop.signalhub.pull.service.generated.openapi.client.interop.api.v1.GatewayApi;
import it.pagopa.interop.signalhub.pull.service.generated.openapi.client.interop.model.v1.Agreement;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;


@Slf4j
@Component
@AllArgsConstructor
public class InteroperabilityClientImpl implements InteroperabilityClient {
    private GatewayApi gatewayApi;


    @Override
    public Mono<Agreement> getAgreementByPurposeId(String purposeId){
        UUID purposeUuid = UUID.fromString(purposeId);
        return gatewayApi.getAgreementByPurpose(purposeUuid);
    }

}