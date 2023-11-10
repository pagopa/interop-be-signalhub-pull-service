package it.pagopa.interop.signalhub.pull.service.service.impl;


import it.pagopa.interop.signalhub.pull.service.auth.PrincipalAgreement;
import it.pagopa.interop.signalhub.pull.service.exception.ExceptionTypeEnum;
import it.pagopa.interop.signalhub.pull.service.exception.PDNDGenericException;
import it.pagopa.interop.signalhub.pull.service.externalclient.InteroperabilityClient;
import it.pagopa.interop.signalhub.pull.service.mapper.PrincipalAgreementMapper;
import it.pagopa.interop.signalhub.pull.service.repository.cache.repository.InteroperabilityCacheRepository;
import it.pagopa.interop.signalhub.pull.service.service.InteropService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@AllArgsConstructor
public class InteropServiceImpl implements InteropService {
    private final InteroperabilityClient interoperabilityClient;
    private final PrincipalAgreementMapper principalAgreementMapper;
    private final InteroperabilityCacheRepository cacheRepository;

    @Override
    public Mono<PrincipalAgreement> getPrincipalFromPurposeId(String purposeId) {
        return cacheRepository.findById(purposeId)
                .doOnNext(cache -> log.info("[{}] purposeId in cache", purposeId))
                .switchIfEmpty(Mono.defer(() -> {
                    log.info("[{}] purposeId no in cache",  purposeId);
                    return interoperabilityClient.getAgreementByPurposeId(purposeId);
                }))
                .flatMap(agreement -> cacheRepository.save(agreement, purposeId))
                .doOnNext(agreement -> log.info("[[{} - {}] Agreement saved on cache", purposeId, agreement.getId()))
                .map(principalAgreementMapper::toPrincipal)
                .onErrorResume(ex ->
                        Mono.error(new PDNDGenericException(ExceptionTypeEnum.DETAIL_AGREEMENT_ERROR,ExceptionTypeEnum.DETAIL_AGREEMENT_ERROR.getMessage())));
    }


}
