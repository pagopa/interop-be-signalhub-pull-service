package it.pagopa.interop.signalhub.pull.service.service.impl;

import it.pagopa.interop.signalhub.pull.service.entities.ConsumerEService;
import it.pagopa.interop.signalhub.pull.service.entities.SignalEntity;
import it.pagopa.interop.signalhub.pull.service.exception.ExceptionTypeEnum;
import it.pagopa.interop.signalhub.pull.service.exception.PDNDGenericException;
import it.pagopa.interop.signalhub.pull.service.mapper.SignalMapper;
import it.pagopa.interop.signalhub.pull.service.repository.ConsumerEServiceRepository;
import it.pagopa.interop.signalhub.pull.service.repository.SignalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class SignalServiceImplTest {
    @InjectMocks
    private SignalServiceImpl signalService;

    @Mock
    private ConsumerEServiceRepository consumerEserviceRepository;
    @Mock
    private SignalRepository signalRepository;
    @Mock
    private SignalMapper signalMapper;



    @Test
    void whenCallPullSignalAndCorrespondenceNotFound() {
        Mockito.when(consumerEserviceRepository.findByConsumerIdAndEServiceId(Mockito.any(), Mockito.any())).thenReturn(Mono.empty());
        StepVerifier.create(signalService.pullSignal("1234","1234",1L))
                .expectErrorMatches((ex) -> {
            assertTrue(ex instanceof PDNDGenericException);
            assertEquals(ExceptionTypeEnum.CORRESPONDENCE_NOT_FOUND,((PDNDGenericException) ex).getExceptionType());
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
        Mockito.when(consumerEserviceRepository.findByConsumerIdAndEServiceId(Mockito.any(), Mockito.any())).thenReturn(Mono.just(consumerEService));

        assertNotNull(signalService.pullSignal("1234","1234",1L));
    }


}
