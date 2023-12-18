package it.pagopa.interop.signalhub.pull.service.cache.repository;

import it.pagopa.interop.signalhub.pull.service.generated.openapi.client.interop.model.v1.Agreement;
import it.pagopa.interop.signalhub.pull.service.generated.openapi.client.interop.model.v1.AgreementState;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveValueOperations;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class InteroperabilityCacheRepositoryTest {
    @InjectMocks
    private InteroperabilityCacheRepository interoperabilityCacheRepository;
    @Mock
    private ReactiveRedisOperations<String, Agreement> reactiveRedisOperations;
    @Mock
    private ReactiveValueOperations<String, Agreement> valueOperations;

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
    void findById() {
        Agreement agreement = getAgreement();
        Mockito.when(reactiveRedisOperations.opsForValue())
                .thenReturn(valueOperations);

        Mockito.when(valueOperations.get(purposeId))
                .thenReturn(Mono.just(agreement));

        interoperabilityCacheRepository.findById(purposeId)
                .flatMap(pa -> {
                    assertEquals(agreement.getEserviceId(), pa.getEserviceId());
                    assertEquals(agreement.getId(), pa.getId());
                    assertEquals(agreement.getConsumerId(), pa.getConsumerId());
                    assertEquals(agreement.getProducerId(), pa.getProducerId());
                    assertEquals(agreement.getDescriptorId(), pa.getDescriptorId());
                    return Mono.empty();
                })
                .block();

        Mockito.when(valueOperations.get(purposeId))
                .thenReturn(Mono.empty());

        StepVerifier.create(interoperabilityCacheRepository.findById(purposeId))
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void save() {
        Agreement agreement = getAgreement();
        Mockito.when(reactiveRedisOperations.opsForValue())
                .thenReturn(valueOperations);

        Mockito.when(valueOperations.set(purposeId, agreement))
                .thenReturn(Mono.just(true));

        assertNotNull(interoperabilityCacheRepository.save(agreement, purposeId)
                .block());
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