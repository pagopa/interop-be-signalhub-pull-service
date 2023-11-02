package it.pagopa.interop.signalhub.pull.service.repository.impl;

import it.pagopa.interop.signalhub.pull.service.entities.ConsumerEService;
import it.pagopa.interop.signalhub.pull.service.entities.EService;
import it.pagopa.interop.signalhub.pull.service.exception.ExceptionTypeEnum;
import it.pagopa.interop.signalhub.pull.service.exception.PDNDGenericException;
import it.pagopa.interop.signalhub.pull.service.mapper.ConsumerEServiceMapper;
import it.pagopa.interop.signalhub.pull.service.mapper.EServiceMapper;
import it.pagopa.interop.signalhub.pull.service.repository.cache.model.ConsumerEServiceCache;
import it.pagopa.interop.signalhub.pull.service.repository.cache.model.EServiceCache;
import it.pagopa.interop.signalhub.pull.service.repository.cache.repository.ConsumerEServiceCacheRepository;
import it.pagopa.interop.signalhub.pull.service.repository.cache.repository.EServiceCacheRepository;
import it.pagopa.interop.signalhub.pull.service.utils.Const;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class EServiceRepositoryImplTest {

    @InjectMocks
    private EServiceRepositoryImpl eServiceRepositoryimpl;

    @Mock
    private EServiceCacheRepository cacheRepository;

    @Mock
    private R2dbcEntityTemplate template;

    @Mock
    private EServiceMapper mapper;

    @Test
    void findByConsumerIdAndEServiceIdButNotExists() {
        Mockito.when(cacheRepository.findById(Mockito.any(), Mockito.any())).thenReturn(Mono.empty());
        Mockito.when(template.selectOne(Mockito.any(), Mockito.any())).thenReturn(Mono.empty());
        assertNull(eServiceRepositoryimpl.checkEServiceStatus("123", "123").block());
    }

    @Test
    void findByConsumerIdAndEServiceIdAndFoundedInCache() {
        EService eService= new EService();
        eService.setEserviceId("123");
        eService.setDescriptorId("123");
        EServiceCache eServiceCache= new EServiceCache();
        eServiceCache.setEserviceId("123");
        eServiceCache.setDescriptorId("123");
        eServiceCache.setState(Const.STATE_ACTIVE);
        Mockito.when(cacheRepository.findById(Mockito.any(), Mockito.any())).thenReturn(Mono.just(eServiceCache));
        Mockito.when(mapper.toEntity(Mockito.any())).thenReturn(eService);
        Mockito.when(template.selectOne(Mockito.any(), Mockito.any())).thenReturn(Mono.empty());
        assertNotNull(eServiceRepositoryimpl.checkEServiceStatus("123", "123").block());
    }

    @Test
    void findByConsumerIdAndEServiceIdButStateIsNotActive() {
        EService eService= new EService();
        eService.setEserviceId("123");
        eService.setDescriptorId("123");
        EServiceCache eServiceCache= new EServiceCache();
        eServiceCache.setEserviceId("123");
        eServiceCache.setDescriptorId("123");
        eServiceCache.setState("test");
        Mockito.when(cacheRepository.findById(Mockito.any(), Mockito.any())).thenReturn(Mono.just(eServiceCache));
        Mockito.when(template.selectOne(Mockito.any(), Mockito.any())).thenReturn(Mono.empty());

        StepVerifier.create(eServiceRepositoryimpl.checkEServiceStatus("1234","1234"))
                .expectErrorMatches((ex) -> {
                    assertTrue(ex instanceof PDNDGenericException);
                    assertEquals(ExceptionTypeEnum.ESERVICE_STATUS_IS_NOT_ACTIVE,((PDNDGenericException) ex).getExceptionType());
                    return true;

                }).verify();
    }

    @Test
    void findByConsumerIdAndEServiceIdButNotFoundInCache() {
        Mockito.when(cacheRepository.findById(Mockito.any(), Mockito.any())).thenReturn(Mono.empty());
        Mockito.when(template.selectOne(Mockito.any(), Mockito.any())).thenReturn(Mono.just(new EService()));
        Mockito.when(cacheRepository.save(Mockito.any())).thenReturn(Mono.just(new EServiceCache()));
        Mockito.when(mapper.toEntity(Mockito.any())).thenReturn(new EService());
        assertNotNull(eServiceRepositoryimpl.checkEServiceStatus("123", "123").block());
    }


}