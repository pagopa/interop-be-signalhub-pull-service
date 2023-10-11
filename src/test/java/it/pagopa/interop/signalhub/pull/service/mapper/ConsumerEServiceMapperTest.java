package it.pagopa.interop.signalhub.pull.service.mapper;

import it.pagopa.interop.signalhub.pull.service.entities.ConsumerEService;
import it.pagopa.interop.signalhub.pull.service.entities.SignalEntity;
import it.pagopa.interop.signalhub.pull.service.repository.cache.model.ConsumerEServiceCache;
import it.pagopa.interop.signalhub.pull.service.rest.v1.dto.Signal;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mapstruct.factory.Mappers;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
class ConsumerEServiceMapperTest {


    private ConsumerEServiceMapper service = Mappers.getMapper(ConsumerEServiceMapper.class);

    @Test
    void toEntity() {
        ConsumerEServiceCache consumerEServiceCache= new ConsumerEServiceCache();
        consumerEServiceCache.setConsumerId("123");
        ConsumerEService consumerEService= service.toEntity(consumerEServiceCache);
        assertEquals(consumerEService.getConsumerId(), consumerEServiceCache.getConsumerId() );
    }

    @Test
    void toCache() {
        ConsumerEService consumerEService= new ConsumerEService();
        consumerEService.setConsumerId("123");
        ConsumerEServiceCache consumerEServiceCache= service.toCache(consumerEService);
        assertEquals(consumerEService.getConsumerId(), consumerEServiceCache.getConsumerId());
    }

    @Test
    void whenCallToEntityAndConsumerEServiceCacheIsNull() {
        ConsumerEServiceCache consumerEServiceCache= null;
        ConsumerEService consumerEService= service.toEntity(consumerEServiceCache);
        assertNull(consumerEService);
    }

    @Test
    void whenCallToCacheAndConsumerEServiceIsNull() {
        ConsumerEService consumerEService= null;
        ConsumerEServiceCache consumerEServiceCache= service.toCache(consumerEService);
        assertNull(consumerEServiceCache);
    }
}