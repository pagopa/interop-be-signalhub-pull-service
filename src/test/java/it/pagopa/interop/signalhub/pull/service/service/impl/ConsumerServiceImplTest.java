package it.pagopa.interop.signalhub.pull.service.service.impl;

import it.pagopa.interop.signalhub.pull.service.entities.ConsumerEService;
import it.pagopa.interop.signalhub.pull.service.exception.ExceptionTypeEnum;
import it.pagopa.interop.signalhub.pull.service.exception.PDNDGenericException;
import it.pagopa.interop.signalhub.pull.service.mapper.ConsumerEServiceMapper;
import it.pagopa.interop.signalhub.pull.service.cache.model.ConsumerEServiceCache;
import it.pagopa.interop.signalhub.pull.service.cache.repository.ConsumerEServiceCacheRepository;
import it.pagopa.interop.signalhub.pull.service.repository.ConsumerEServiceRepository;
import it.pagopa.interop.signalhub.pull.service.utils.Const;
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
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ConsumerServiceImplTest {

    @InjectMocks
    private ConsumerServiceImpl consumerService;

    @Mock
    private ConsumerEServiceCacheRepository cacheRepository;

    @Mock
    private ConsumerEServiceMapper mapper;

    @Mock
    private ConsumerEServiceRepository consumerEServiceRepository;

    @Test
    void findByConsumerIdAndEServiceIdButNotExists() {
        Mockito.when(cacheRepository.findById(Mockito.any(), Mockito.any())).thenReturn(Mono.empty());
        Mockito.when(consumerEServiceRepository.findByConsumerIdAndEServiceIdAndState(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(Flux.empty());

        StepVerifier.create(consumerService.getConsumerEservice("1234","1234"))
                .expectErrorMatches((ex) -> {
                    assertTrue(ex instanceof PDNDGenericException);
                    assertEquals(ExceptionTypeEnum.CONSUMER_ESERVICE_NOT_FOUND,((PDNDGenericException) ex).getExceptionType());
                    return true;

                }).verify();
    }


    @Test
    void findByConsumerIdAndEServiceIdAndFoundedInCache() {
        //case 1: state is active
        ConsumerEService consumerEService= new ConsumerEService();
        consumerEService.setEserviceId("123");
        consumerEService.setConsumerId("123");
        ConsumerEServiceCache consumerEServiceCache= new ConsumerEServiceCache();
        consumerEServiceCache.setEserviceId("123");
        consumerEServiceCache.setConsumerId("123");
        consumerEServiceCache.setState(Const.STATE_ACTIVE);
        Mockito.when(cacheRepository.findById(Mockito.any(), Mockito.any())).thenReturn(Mono.just(consumerEServiceCache));
        Mockito.when(mapper.toEntity(Mockito.any())).thenReturn(consumerEService);
        Mockito.when(consumerEServiceRepository.findByConsumerIdAndEServiceIdAndState(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(Flux.empty());
        assertNotNull(consumerService.getConsumerEservice("123", "123").block());

        //case 2: founded in cache but state is not active
        consumerEServiceCache.setState(Const.STATE_PUBLISHED);
        StepVerifier.create(consumerService.getConsumerEservice("1234","1234"))
                .expectErrorMatches((ex) -> {
                    assertTrue(ex instanceof PDNDGenericException);
                    assertEquals(ExceptionTypeEnum.CONSUMER_STATUS_IS_NOT_ACTIVE,((PDNDGenericException) ex).getExceptionType());
                    return true;

                }).verify();
    }

    @Test
    void findByConsumerIdAndEServiceIdButNotFoundInCache() {
        Mockito.when(cacheRepository.findById(Mockito.any(), Mockito.any())).thenReturn(Mono.empty());
        Mockito.when(consumerEServiceRepository.findByConsumerIdAndEServiceIdAndState(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(Flux.just(new ConsumerEService()));
        Mockito.when(cacheRepository.save(Mockito.any())).thenReturn(Mono.just(new ConsumerEServiceCache()));
        Mockito.when(mapper.toEntity(Mockito.any())).thenReturn(new ConsumerEService());
        assertNotNull(consumerService.getConsumerEservice("123", "123").block());
    }
}