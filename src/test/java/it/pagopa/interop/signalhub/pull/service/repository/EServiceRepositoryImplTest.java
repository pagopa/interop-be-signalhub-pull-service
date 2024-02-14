package it.pagopa.interop.signalhub.pull.service.repository;

import it.pagopa.interop.signalhub.pull.service.config.BaseTest;
import it.pagopa.interop.signalhub.pull.service.entities.EService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

class EServiceRepositoryImplTest extends BaseTest.WithR2DBC {
    private static final String correctEservice = "BC-eservice";
    private static final String correctDescriptor = "BC-descriptor";
    private static final String correctState = "PUBLISHED";
    private static final String incorrectState = "SUSPENDED";
    @Autowired
    private EServiceRepository eServiceRepository;

    @Test
    void whenFindOrganizationWithBadlyParamThenReturnNull(){
        List<EService> entity =
                eServiceRepository.checkEServiceStatus(
                        correctEservice,
                        correctDescriptor,
                        incorrectState).collectList().block();
        Assertions.assertNotNull(entity);
        Assertions.assertTrue(entity.isEmpty());
    }


    @Test
    void whenFindOrganizationWithCorrectParamThenReturnEntity(){
        List<EService> entity =
                eServiceRepository.checkEServiceStatus(
                        correctEservice,
                        correctDescriptor,
                        correctState).collectList().block();
        Assertions.assertNotNull(entity);
        Assertions.assertFalse(entity.isEmpty());
        Assertions.assertEquals(entity.get(0), getEntity());
    }


    private EService getEntity(){
        EService entity = new EService();
        entity.setEserviceId(correctEservice);
        entity.setProducerId("BC-producer");
        entity.setState(correctState);
        entity.setEventId(12L);
        entity.setDescriptorId(correctDescriptor);

        return entity;
    }



}