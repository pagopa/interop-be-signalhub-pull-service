package it.pagopa.interop.signalhub.pull.service.service.impl;


import it.pagopa.interop.signalhub.pull.service.auth.PrincipalAgreement;
import it.pagopa.interop.signalhub.pull.service.exception.PDNDGenericException;
import it.pagopa.interop.signalhub.pull.service.externalclient.InteroperabilityClient;
import it.pagopa.interop.signalhub.pull.service.mapper.PrincipalAgreementMapper;
import it.pagopa.interop.signalhub.pull.service.service.InteropService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static it.pagopa.interop.signalhub.pull.service.exception.ExceptionTypeEnum.DETAIL_AGREEMENT_ERROR;


@Service
@AllArgsConstructor
public class InteropServiceImpl implements InteropService {
    private final InteroperabilityClient interoperabilityClient;
    private final PrincipalAgreementMapper principalAgreementMapper;

    @Override
    public Mono<PrincipalAgreement> getPrincipalFromPurposeId(String purposeId) {
        return interoperabilityClient.getAgreementByPurposeId(purposeId)
                .map(principalAgreementMapper::toPrincipal)
                .onErrorResume(ex ->
                        Mono.error(new PDNDGenericException(DETAIL_AGREEMENT_ERROR, DETAIL_AGREEMENT_ERROR.getMessage())));
    }


}
