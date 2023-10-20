package it.pagopa.interop.signalhub.pull.service.repository.impl;

import it.pagopa.interop.signalhub.pull.service.entities.EService;
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
class EServiceRepositoryImplTest {

    @InjectMocks
    private EServiceRepositoryImpl eServiceRepositoryimpl;

    @Mock
    private R2dbcEntityTemplate template;

    @Test
    void checkEServiceStatusAndEServiceIdButNotExists() {
        Mockito.when(template.selectOne(Mockito.any(), Mockito.any())).thenReturn(Mono.empty());
        assertNull(eServiceRepositoryimpl.checkEServiceStatus("123").block());
    }

    @Test
    void findByPbroducerIdAndEServiceIdAndFoundedInCache() {
        EService eService= new EService();
        eService.setEserviceId("123");
        Mockito.when(template.selectOne(Mockito.any(), Mockito.any())).thenReturn(Mono.just(new EService()));
        assertNotNull(eServiceRepositoryimpl.checkEServiceStatus("123").block());
    }


}