package it.pagopa.interop.signalhub.pull.service.repository.impl;

import it.pagopa.interop.signalhub.pull.service.entities.ConsumerEService;
import it.pagopa.interop.signalhub.pull.service.mapper.ConsumerEServiceMapper;
import it.pagopa.interop.signalhub.pull.service.repository.cache.model.ConsumerEServiceCache;
import it.pagopa.interop.signalhub.pull.service.repository.cache.repository.ConsumerEServiceCacheRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class ConsumerEServiceRepositoryImplTest {

    @InjectMocks
    private ConsumerEServiceRepositoryImpl consumerEServiceRepository;

    @Mock
    private ConsumerEServiceCacheRepository cacheRepository;

    @Mock
    private ConsumerEServiceMapper mapper;

    @Mock
    private R2dbcEntityTemplate template;

    @Test
    void findByProducerIdAndEServiceIdButNotExists() {
        Mockito.when(cacheRepository.findById(Mockito.any(), Mockito.any())).thenReturn(Mono.empty());
        Mockito.when(template.selectOne(Mockito.any(), Mockito.any())).thenReturn(Mono.empty());
        assertNull(consumerEServiceRepository.findByConsumerIdAndEServiceId("123", "123").block());
    }

    @Test
    void findByPbroducerIdAndEServiceIdAndFoundedInCache() {
        ConsumerEService eService= new ConsumerEService();
        eService.setEserviceId("123");
        eService.setConsumerId("123");
        ConsumerEServiceCache eServiceCache= new ConsumerEServiceCache();
        eServiceCache.setEserviceId("123");
        eServiceCache.setConsumerId("123");
        Mockito.when(cacheRepository.findById(Mockito.any(), Mockito.any())).thenReturn(Mono.just(eServiceCache));
        Mockito.when(mapper.toEntity(Mockito.any())).thenReturn(eService);
        Mockito.when(template.selectOne(Mockito.any(), Mockito.any())).thenReturn(Mono.empty());
        assertNotNull(consumerEServiceRepository.findByConsumerIdAndEServiceId("123", "123").block());
    }

    @Test
    void findByPbroducerIdAndEServiceIdButNotFoundInCache() {
        Mockito.when(cacheRepository.findById(Mockito.any(), Mockito.any())).thenReturn(Mono.empty());
        Mockito.when(template.selectOne(Mockito.any(), Mockito.any())).thenReturn(Mono.just(new ConsumerEService()));
        Mockito.when(cacheRepository.save(Mockito.any())).thenReturn(Mono.just(new ConsumerEServiceCache()));
        Mockito.when(mapper.toEntity(Mockito.any())).thenReturn(new ConsumerEService());
        assertNotNull(consumerEServiceRepository.findByConsumerIdAndEServiceId("123", "123").block());
    }
}