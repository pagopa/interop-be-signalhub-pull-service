package it.pagopa.interop.signalhub.pull.service.service.impl;

import it.pagopa.interop.signalhub.pull.service.entities.EService;
import it.pagopa.interop.signalhub.pull.service.exception.ExceptionTypeEnum;
import it.pagopa.interop.signalhub.pull.service.exception.PDNDGenericException;
import it.pagopa.interop.signalhub.pull.service.mapper.EServiceMapper;
import it.pagopa.interop.signalhub.pull.service.cache.model.EServiceCache;
import it.pagopa.interop.signalhub.pull.service.cache.repository.EServiceCacheRepository;
import it.pagopa.interop.signalhub.pull.service.repository.EServiceRepository;
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
class OrganizationServiceImplTest {

    @InjectMocks
    private OrganizationServiceImpl organizationService;

    @Mock
    private EServiceCacheRepository cacheRepository;

    @Mock
    private EServiceRepository eServiceRepository;

    @Mock
    private EServiceMapper mapper;

    @Test
    void findByConsumerIdAndEServiceIdButNotExists() {
        Mockito.when(cacheRepository.findById(Mockito.any(), Mockito.any())).thenReturn(Mono.empty());
        Mockito.when(eServiceRepository.checkEServiceStatus(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(Flux.empty());

        StepVerifier.create(organizationService.getEService("1234","1234"))
                .expectErrorMatches((ex) -> {
                    assertTrue(ex instanceof PDNDGenericException);
                    assertEquals(ExceptionTypeEnum.UNAUTHORIZED,((PDNDGenericException) ex).getExceptionType());
                    return true;

                }).verify();
    }

    @Test
    void findByConsumerIdAndEServiceIdAndFoundedInCache() {
        EService eService= new EService();
        eService.setEserviceId("123");
        eService.setDescriptorId("123");
        EServiceCache eServiceCache= new EServiceCache();
        eServiceCache.setEserviceId("123");
        eServiceCache.setDescriptorId("123");
        eServiceCache.setState(Const.STATE_PUBLISHED);
        Mockito.when(cacheRepository.findById(Mockito.any(), Mockito.any())).thenReturn(Mono.just(eServiceCache));
        Mockito.when(mapper.toEntity(Mockito.any())).thenReturn(eService);
        Mockito.when(eServiceRepository.checkEServiceStatus(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(Flux.empty());

        assertNotNull(organizationService.getEService("123", "123").block());

    }

    @Test
    void findByConsumerIdAndEServiceIdButStateIsNotPublished() {
        EService eService = new EService();
        eService.setEserviceId("123");
        eService.setDescriptorId("123");
        EServiceCache eServiceCache= new EServiceCache();
        eServiceCache.setEserviceId("123");
        eServiceCache.setDescriptorId("123");
        eServiceCache.setState("test");
        Mockito.when(cacheRepository.findById(Mockito.any(), Mockito.any())).thenReturn(Mono.just(eServiceCache));
        Mockito.when(eServiceRepository.checkEServiceStatus(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(Flux.empty());

        StepVerifier.create(organizationService.getEService("1234","1234"))
                .expectErrorMatches((ex) -> {
                    assertTrue(ex instanceof PDNDGenericException);
                    assertEquals(ExceptionTypeEnum.UNAUTHORIZED,((PDNDGenericException) ex).getExceptionType());
                    return true;

                }).verify();
    }

    @Test
    void findByConsumerIdAndEServiceIdButNotFoundInCache() {
        Mockito.when(cacheRepository.findById(Mockito.any(), Mockito.any())).thenReturn(Mono.empty());
        Mockito.when(eServiceRepository.checkEServiceStatus(Mockito.any(), Mockito.any(), Mockito.any())).thenReturn(Flux.just(new EService()));
        Mockito.when(cacheRepository.save(Mockito.any())).thenReturn(Mono.just(new EServiceCache()));
        Mockito.when(mapper.toEntity(Mockito.any())).thenReturn(new EService());
        assertNotNull(organizationService.getEService("123", "123").block());
    }


}