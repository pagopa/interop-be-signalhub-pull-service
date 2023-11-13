package it.pagopa.interop.signalhub.pull.service.cache.repository;

import it.pagopa.interop.signalhub.pull.service.cache.model.EServiceCache;
import it.pagopa.interop.signalhub.pull.service.cache.repository.EServiceCacheRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.ReactiveListOperations;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class EServiceCacheRepositoryTest {
    @InjectMocks
    private EServiceCacheRepository eServiceCacheRepository;

    @Mock
    private ReactiveRedisOperations<String, EServiceCache> reactiveRedisOperations;

    @Mock
    private ReactiveListOperations<String, EServiceCache> listOperations;

    private EServiceCache eserviceCache;

    @BeforeEach
    void inizialize(){
        eserviceCache = new EServiceCache();
        eserviceCache.setEserviceId("123");
        eserviceCache.setProducerId("123");
        Mockito.when(reactiveRedisOperations.opsForList()).thenReturn(listOperations);
    }

    @Test
    void findById() {
        Mockito.when(reactiveRedisOperations.opsForList().indexOf(Mockito.any(), Mockito.any())).thenReturn(Mono.just(1l));
        Mockito.when(reactiveRedisOperations.opsForList().index(Mockito.any(), Mockito.anyLong())).thenReturn(Mono.just(eserviceCache));
        assertNotNull(eServiceCacheRepository.findById("123",  "123").block());
    }



    @Test
    void save() {
        Mockito.when(reactiveRedisOperations.opsForList().leftPush(Mockito.anyString(), Mockito.any())).thenReturn(Mono.just(1l));
        assertNotNull(eServiceCacheRepository.save(eserviceCache).block());
    }

    @Test
    void callSaveButReturnEmpty() {
        Mockito.when(reactiveRedisOperations.opsForList().leftPush(Mockito.anyString(), Mockito.any())).thenReturn(Mono.empty());
        assertNotNull(eServiceCacheRepository.save(eserviceCache).block());
    }
}