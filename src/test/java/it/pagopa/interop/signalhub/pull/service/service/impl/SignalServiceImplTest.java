package it.pagopa.interop.signalhub.pull.service.service.impl;

import it.pagopa.interop.signalhub.pull.service.entities.ConsumerEService;
import it.pagopa.interop.signalhub.pull.service.entities.EService;
import it.pagopa.interop.signalhub.pull.service.entities.SignalEntity;
import it.pagopa.interop.signalhub.pull.service.exception.ExceptionTypeEnum;
import it.pagopa.interop.signalhub.pull.service.exception.PDNDGenericException;
import it.pagopa.interop.signalhub.pull.service.mapper.SignalMapper;
import it.pagopa.interop.signalhub.pull.service.repository.SignalRepository;
import it.pagopa.interop.signalhub.pull.service.rest.v1.dto.Signal;
import it.pagopa.interop.signalhub.pull.service.service.ConsumerService;
import it.pagopa.interop.signalhub.pull.service.service.OrganizationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SignalServiceImplTest {
    @InjectMocks
    private SignalServiceImpl signalService;

    @Mock
    private ConsumerService consumerService;
    @Mock
    private SignalRepository signalRepository;
    @Mock
    private SignalMapper signalMapper;
    @Mock
    private OrganizationService organizationService;




    @Test
    void whenCallPullSignalAndCorrespondenceNotFound() {
        Mockito.when(consumerService.getConsumerEservice(Mockito.any(), Mockito.any())).thenReturn(Mono.empty());
        StepVerifier.create(signalService.pullSignal("1234","1234",1L, 1L))
                .expectErrorMatches((ex) -> {
                    assertTrue(ex instanceof PDNDGenericException);
                    assertEquals(ExceptionTypeEnum.UNAUTHORIZED,((PDNDGenericException) ex).getExceptionType());
                    return true;

                }).verify();

    }

    @Test
    void whenCallPushSignalAndSignalIdAlreadyExist() {
        SignalEntity signal= new SignalEntity();
        signal.setSignalId(1L);
        ConsumerEService consumerEService= new ConsumerEService();
        consumerEService.setEserviceId("123");
        consumerEService.setConsumerId("123");
        Mockito.when(consumerService.getConsumerEservice(Mockito.any(), Mockito.any())).thenReturn(Mono.just(consumerEService));
        Mockito.when(organizationService.getEService(Mockito.any(), Mockito.any())).thenReturn(Mono.just(new EService()));
        Mockito.when(signalRepository.findSignal(Mockito.any(),Mockito.any(), Mockito.any())).thenReturn(Flux.just(new SignalEntity()));
        Mockito.when(signalMapper.toDto(Mockito.any())).thenReturn(new Signal());

        assertNotNull(signalService.pullSignal("1234","1234",1L, 1L).blockLast());
    }

    @Test
    void whenCounterReturnBiggestSignalId() {
        Mockito.when(signalRepository.maxSignal(Mockito.any()))
                .thenReturn(Mono.just(8));

        signalService.counter(Mockito.any())
                .flatMap(integer -> {
                    Assertions.assertEquals(8, integer.intValue());
                    return Mono.empty();
                })
                .block();
    }
}
