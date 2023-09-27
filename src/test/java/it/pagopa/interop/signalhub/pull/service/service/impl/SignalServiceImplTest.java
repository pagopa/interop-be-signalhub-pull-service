package it.pagopa.interop.signalhub.pull.service.service.impl;

import it.pagopa.interop.signalhub.pull.service.entities.EService;
import it.pagopa.interop.signalhub.pull.service.exception.ExceptionTypeEnum;
import it.pagopa.interop.signalhub.pull.service.exception.PnGenericException;
import it.pagopa.interop.signalhub.pull.service.repository.EServiceRepository;
import it.pagopa.interop.signalhub.pull.service.repository.SignalRepository;
import it.pagopa.interop.signalhub.pull.service.rest.v1.dto.Signal;
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
public class SignalServiceImplTest {
    @InjectMocks
    private SignalServiceImpl signalService;

    @Mock
    private EServiceRepository eServiceRepository;
    @Mock
    private SignalRepository signalRepository;


    @Test
    void whenCallPullSignalAndCorrespondenceNotFound() {

        Mockito.when(eServiceRepository.findByOrganizationIdAndEServiceId(Mockito.any(), Mockito.any())).thenReturn(Mono.empty());
        StepVerifier.create(signalService.pullSignal("1234","1234",1L))
                .expectErrorMatches((ex) -> {
            assertTrue(ex instanceof PnGenericException);
            assertEquals(ExceptionTypeEnum.CORRESPONDENCE_NOT_FOUND,((PnGenericException) ex).getExceptionType());
            return true;

        }).verify();

    }

    @Test
    void whenCallPullSignalAndSignalIdAlreadyExists() {
        Mockito.when(eServiceRepository.findByOrganizationIdAndEServiceId(Mockito.any(), Mockito.any())).thenReturn(Mono.just(new EService()));
        Mockito.when(signalRepository.findSignal(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(Mono.empty());

        StepVerifier.create(signalService.pullSignal("1234","1234",1L))
                .expectErrorMatches((ex) -> {
                    assertTrue(ex instanceof PnGenericException);
                    assertEquals(ExceptionTypeEnum.SIGNALID_ALREADY_EXISTS, ((PnGenericException) ex).getExceptionType());
                    return true;
                }).verify();
    }

    @Test
    void whenCallPushSignalAndSignalIdAlreadyExist() {
        EService eService= new EService();
        eService.setEserviceId("123");
        eService.setOrganizationId("123");
        Mockito.when(eServiceRepository.findByOrganizationIdAndEServiceId(Mockito.any(), Mockito.any())).thenReturn(Mono.just(eService));

        assertNotNull(signalService.pullSignal("1234","1234",1L));
    }


}
