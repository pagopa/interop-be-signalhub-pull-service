package it.pagopa.interop.signalhub.pull.service.repository.cache.repository;

import it.pagopa.interop.signalhub.pull.service.repository.cache.model.JWTCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.ReactiveListOperations;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


@ExtendWith(MockitoExtension.class)
class JWTCacheRepositoryTest {

    @InjectMocks
    private JWTCacheRepository jwtCacheRepository;

    @Mock
    private ReactiveRedisOperations<String, JWTCache> reactiveRedisOperations;

    @Mock
    private ReactiveListOperations<String, JWTCache> listOperations;

    private JWTCache jwtCache;

    @BeforeEach
    void inizialize(){
        jwtCache= new JWTCache();
        jwtCache.setJwt("jwt");
        Mockito.when(reactiveRedisOperations.opsForList()).thenReturn(listOperations);
    }

    @Test
    void findById() {

        Mockito.when(reactiveRedisOperations.opsForList().range(Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong())).thenReturn(Flux.just(jwtCache));
        assertNotNull(jwtCacheRepository.findById("jwt").block());
    }

    @Test
    void findByIdButReturnNull() {
        Mockito.when(reactiveRedisOperations.opsForList().range(Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong())).thenReturn(Flux.empty());
        assertNull(jwtCacheRepository.findById("123").block());
    }

    @Test
    void save() {
        Mockito.when(reactiveRedisOperations.opsForList().rightPush(Mockito.anyString(), Mockito.any())).thenReturn(Mono.just(1l));
        assertNotNull(jwtCacheRepository.save(jwtCache).block());
    }
}