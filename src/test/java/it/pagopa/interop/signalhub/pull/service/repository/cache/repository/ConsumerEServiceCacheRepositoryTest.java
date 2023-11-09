package it.pagopa.interop.signalhub.pull.service.repository.cache.repository;

import it.pagopa.interop.signalhub.pull.service.repository.cache.model.ConsumerEServiceCache;
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

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ConsumerEServiceCacheRepositoryTest {

    @InjectMocks
    private ConsumerEServiceCacheRepository consumerEServiceCacheRepository;

    @Mock
    private ReactiveRedisOperations<String, ConsumerEServiceCache> reactiveRedisOperations;

    @Mock
    private ReactiveListOperations<String, ConsumerEServiceCache> listOperations;

    private ConsumerEServiceCache consumerEServiceCache;

    @BeforeEach
    void inizialize(){
        consumerEServiceCache = new ConsumerEServiceCache();
        consumerEServiceCache.setEserviceId("123");
        consumerEServiceCache.setConsumerId("123");
        Mockito.when(reactiveRedisOperations.opsForList()).thenReturn(listOperations);
    }

    @Test
    void findById() {
        Mockito.when(reactiveRedisOperations.opsForList().indexOf(Mockito.any(), Mockito.any())).thenReturn(Mono.just(1l));
        Mockito.when(reactiveRedisOperations.opsForList().index(Mockito.any(), Mockito.anyLong())).thenReturn(Mono.just(consumerEServiceCache));
        assertNotNull(consumerEServiceCacheRepository.findById("123",  "123").block());
    }



    @Test
    void save() {
        Mockito.when(reactiveRedisOperations.opsForList().leftPush(Mockito.anyString(), Mockito.any())).thenReturn(Mono.just(1l));
        assertNotNull(consumerEServiceCacheRepository.save(consumerEServiceCache).block());
    }

    @Test
    void callSaveButReturnEmpty() {
        Mockito.when(reactiveRedisOperations.opsForList().leftPush(Mockito.anyString(), Mockito.any())).thenReturn(Mono.empty());
        assertNotNull(consumerEServiceCacheRepository.save(consumerEServiceCache).block());
    }

}