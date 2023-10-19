//package it.pagopa.interop.signalhub.pull.service.repository.cache.repository;
//
//import it.pagopa.interop.signalhub.pull.service.repository.cache.model.ConsumerEServiceCache;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.redis.core.ReactiveListOperations;
//import org.springframework.data.redis.core.ReactiveRedisOperations;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertNull;
//
//@ExtendWith(MockitoExtension.class)
//class EServiceCacheRepositoryTest {
//    @InjectMocks
//    private ConsumerEServiceCacheRepository eServiceCacheRepository;
//
//    @Mock
//    private ReactiveRedisOperations<String, ConsumerEServiceCache> reactiveRedisOperations;
//
//    @Mock
//    private ReactiveListOperations<String, ConsumerEServiceCache> listOperations;
//
//    private ConsumerEServiceCache eServiceCache;
//
//    @BeforeEach
//    void inizialize(){
//        eServiceCache= new ConsumerEServiceCache();
//        eServiceCache.setEserviceId("123");
//        eServiceCache.setConsumerId("123");
//        Mockito.when(reactiveRedisOperations.opsForValue()).thenReturn(listOperations);
//    }
//
//    @Test
//    void findById() {
//        Mockito.when(reactiveRedisOperations.opsForList().range(Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong())).thenReturn(Flux.just(eServiceCache));
//        assertNotNull(eServiceCacheRepository.findById("123", "123").block());
//    }
//
//    @Test
//    void findByIdButReturnNull() {
//        Mockito.when(reactiveRedisOperations.opsForList().range(Mockito.anyString(), Mockito.anyLong(), Mockito.anyLong())).thenReturn(Flux.empty());
//        assertNull(eServiceCacheRepository.findById("123", "123").block());
//    }
//
//    @Test
//    void save() {
//        Mockito.when(reactiveRedisOperations.opsForList().rightPush(Mockito.anyString(), Mockito.any())).thenReturn(Mono.just(1l));
//        assertNotNull(eServiceCacheRepository.save(eServiceCache).block());
//    }
//}