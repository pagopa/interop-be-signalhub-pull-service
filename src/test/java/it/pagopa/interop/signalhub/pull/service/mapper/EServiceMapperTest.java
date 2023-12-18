package it.pagopa.interop.signalhub.pull.service.mapper;

import it.pagopa.interop.signalhub.pull.service.entities.EService;
import it.pagopa.interop.signalhub.pull.service.cache.model.EServiceCache;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class EServiceMapperTest {


    private EServiceMapper service = Mappers.getMapper(EServiceMapper.class);

    @Test
    void toEntity() {
        EServiceCache eServiceCache= new EServiceCache();
        eServiceCache.setEserviceId("123");
        EService eService= service.toEntity(eServiceCache);
        assertEquals(eServiceCache.getEserviceId(), eService.getEserviceId() );
    }

    @Test
    void whenCallToEntityAndEServiceCacheIsNull() {
        EServiceCache eServiceCache= null;
        EService eService= service.toEntity(eServiceCache);
        assertNull(eService);
    }

    @Test
    void toCache() {
        EService eService= new EService();
        eService.setEserviceId("123");
        EServiceCache eServiceCache= service.toCache(eService);
        assertEquals(eServiceCache.getEserviceId(), eService.getEserviceId());
    }

    @Test
    void whenCallToCacheAndEserviceIsNull() {
        EService eService= null;
        EServiceCache eServiceCache= service.toCache(eService);
        assertNull(eServiceCache);
    }

}