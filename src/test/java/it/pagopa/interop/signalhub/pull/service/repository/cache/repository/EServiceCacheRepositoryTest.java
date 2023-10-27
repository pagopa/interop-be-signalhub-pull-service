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
//import org.springframework.data.redis.core.ReactiveRedisOperations;
//import org.springframework.data.redis.core.ReactiveValueOperations;
//import reactor.core.publisher.Mono;
//
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertNull;
//
//@ExtendWith(MockitoExtension.class)
//class EServiceCacheRepositoryTest {
//    @InjectMocks
//    private ConsumerEServiceCacheRepository consumerEServiceCacheRepository;
//
//    @Mock
//    private ReactiveRedisOperations<String, ConsumerEServiceCache> reactiveRedisOperations;
//
//    @Mock
//    private ReactiveValueOperations<String, ConsumerEServiceCache> valueOperations;
//
//    private ConsumerEServiceCache consumerEServiceCache;
//
//    @BeforeEach
//    void inizialize(){
//        consumerEServiceCache = new ConsumerEServiceCache();
//        consumerEServiceCache.setEserviceId("123");
//        consumerEServiceCache.setConsumerId("123");
//        Mockito.when(reactiveRedisOperations.opsForValue()).thenReturn(valueOperations);
//    }
//
//    @Test
//    void findById() {
//        Mockito.when(reactiveRedisOperations.opsForValue().get(Mockito.anyString())).thenReturn(Mono.just(consumerEServiceCache));
//        assertNotNull(consumerEServiceCacheRepository.findById("123",  "123").block());
//    }
//
//    @Test
//    void findByIdButReturnNull() {
//        Mockito.when(reactiveRedisOperations.opsForValue().get(Mockito.anyString())).thenReturn(Mono.empty());
//        assertNull(consumerEServiceCacheRepository.findById("123",  "123").block());
//    }
//
//    @Test
//    void save() {
//        Mockito.when(reactiveRedisOperations.opsForValue().set(Mockito.anyString(), Mockito.any())).thenReturn(Mono.just(true));
//        assertNotNull(consumerEServiceCacheRepository.save(consumerEServiceCache).block());
//    }
//}