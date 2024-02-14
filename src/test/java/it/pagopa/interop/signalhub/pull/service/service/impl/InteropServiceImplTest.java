package it.pagopa.interop.signalhub.pull.service.service.impl;

import it.pagopa.interop.signalhub.pull.service.auth.PrincipalAgreement;
import it.pagopa.interop.signalhub.pull.service.cache.repository.InteroperabilityCacheRepository;
import it.pagopa.interop.signalhub.pull.service.exception.ExceptionTypeEnum;
import it.pagopa.interop.signalhub.pull.service.exception.PDNDGenericException;
import it.pagopa.interop.signalhub.pull.service.externalclient.InteroperabilityClient;
import it.pagopa.interop.signalhub.pull.service.generated.openapi.client.interop.model.v1.Agreement;
import it.pagopa.interop.signalhub.pull.service.generated.openapi.client.interop.model.v1.AgreementState;
import it.pagopa.interop.signalhub.pull.service.mapper.PrincipalAgreementMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;


@ExtendWith(MockitoExtension.class)
class InteropServiceImplTest {
    @InjectMocks
    private InteropServiceImpl interopService;
    @Mock
    private InteroperabilityClient interoperabilityClient;
    @Mock
    private PrincipalAgreementMapper principalAgreementMapper;
    @Mock
    private InteroperabilityCacheRepository cacheRepository;
    private String principalId;
    private String purposeId;
    private String eServiceId;
    private String producerId;
    private String descriptorId;
    private AgreementState state;

    @BeforeEach
    void preTest() {
        setUp();
    }

    @Test
    void getPrincipalFromPurposeIdWhenAgreementWasFoundTest() {
        Agreement agreement = getAgreement();
        PrincipalAgreement principalAgreement = getPrincipalAgreement();

        Mockito.when(cacheRepository.findById(purposeId))
                .thenReturn(Mono.just(agreement));
        Mockito.when(cacheRepository.save(agreement, purposeId))
                .thenReturn(Mono.just(agreement));
        Mockito.when(principalAgreementMapper.toPrincipal(agreement))
                .thenReturn(principalAgreement);

        interopService.getPrincipalFromPurposeId(purposeId)
                .flatMap(pa -> {
                    Assertions.assertEquals(purposeId, pa.getPurposeId());
                    Assertions.assertEquals(principalId, pa.getPrincipalId());
                    Assertions.assertEquals(eServiceId, pa.getEServiceId());
                    Assertions.assertEquals(producerId, pa.getProducerId());
                    Assertions.assertEquals(descriptorId, pa.getDescriptorId());
                    Assertions.assertEquals(state.getValue(), pa.getState());
                    return Mono.empty();
                })
                .block();
    }

    @Test
    void getPrincipalFromPurposeIdWhenNoAgreementWasFoundTest() {
        Agreement agreement = getAgreement();
        PrincipalAgreement principalAgreement = getPrincipalAgreement();

        Mockito.when(cacheRepository.findById(purposeId))
                .thenReturn(Mono.empty());
        Mockito.when(interoperabilityClient.getAgreementByPurposeId(purposeId))
                .thenReturn(Mono.just(agreement));
        Mockito.when(cacheRepository.save(agreement, purposeId))
                .thenReturn(Mono.just(agreement));
        Mockito.when(principalAgreementMapper.toPrincipal(agreement))
                .thenReturn(principalAgreement);

        interopService.getPrincipalFromPurposeId(purposeId)
                .flatMap(pa -> {
                    Assertions.assertEquals(purposeId, pa.getPurposeId());
                    Assertions.assertEquals(principalId, pa.getPrincipalId());
                    Assertions.assertEquals(eServiceId, pa.getEServiceId());
                    Assertions.assertEquals(producerId, pa.getProducerId());
                    Assertions.assertEquals(descriptorId, pa.getDescriptorId());
                    Assertions.assertEquals(state.getValue(), pa.getState());
                    return Mono.empty();
                })
                .block();
    }

    @Test
    void getPrincipalFromPurposeIdWhenNoAgreementWasFoundAndClientReturnErrorTest() {
        Mockito.when(cacheRepository.findById(purposeId))
                .thenReturn(Mono.empty());
        Mockito.when(interoperabilityClient.getAgreementByPurposeId(purposeId))
                .thenThrow(new PDNDGenericException(ExceptionTypeEnum.DETAIL_AGREEMENT_ERROR,ExceptionTypeEnum.DETAIL_AGREEMENT_ERROR.getMessage()));

        StepVerifier.create(interopService.getPrincipalFromPurposeId(purposeId))
                .expectErrorMatches(exception -> {
                    Assertions.assertEquals(exception.getClass(), PDNDGenericException.class);
                    Assertions.assertEquals(exception.getMessage(), ExceptionTypeEnum.DETAIL_AGREEMENT_ERROR.getMessage());
                    return true;
                }).verify();
    }

    private PrincipalAgreement getPrincipalAgreement() {
        PrincipalAgreement principalAgreement = new PrincipalAgreement();
        principalAgreement.setPrincipalId(principalId);
        principalAgreement.setPurposeId(purposeId);
        principalAgreement.setEServiceId(eServiceId);
        principalAgreement.setProducerId(producerId);
        principalAgreement.setDescriptorId(descriptorId);
        principalAgreement.setState(state.getValue());
        return principalAgreement;
    }

    private Agreement getAgreement() {
        Agreement agreement = new Agreement();
        agreement.setId(UUID.fromString(principalId));
        agreement.setEserviceId(UUID.fromString(purposeId));
        agreement.setDescriptorId(UUID.fromString(eServiceId));
        agreement.setProducerId(UUID.fromString(producerId));
        agreement.setConsumerId(UUID.fromString(descriptorId));
        agreement.setState(state);
        return agreement;
    }

    private void setUp() {
        principalId = "0f14d0ab-9605-4a62-a9e4-5ed26688389b";
        purposeId = "4a620f14-d0ab-9605-a9e4-5ed26688389b";
        eServiceId = "5ed26688-389b-4a62-9605-0f14d0ab9605";
        producerId = "8a631cab-797b-482d-bf7d-615bf1004368";
        descriptorId = "01920f14-d0ab-9605-a9e4-374650192842";
        state = AgreementState.ARCHIVED;
    }
}