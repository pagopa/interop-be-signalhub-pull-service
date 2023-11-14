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
    private static final String correctEservice = "1234";
    private static final String correctDescriptor = "1234";
    private static final String correctState = "PUBLISHED";
    private static final String incorrectState = "ACTIVE";

    @Autowired
    private EServiceRepository eServiceRepository;

    @BeforeEach
    void setUp(){
        eServiceRepository.save(getEntity());
    }

    @Test
    void whenFindOrganizationWithBadlyParamThenReturnNull(){
        List<EService> entity =
                eServiceRepository.checkEServiceStatus(
                        correctEservice,
                        correctDescriptor,
                        incorrectState).collectList().block();

        Assertions.assertTrue(entity.isEmpty());
    }


    @Test
    void whenFindOrganizationWithCorrectParamThenReturnEntity(){
        eServiceRepository.save(getEntity()).block();
        Assertions.assertFalse(eServiceRepository.findAll().collectList().block().isEmpty());
        List<EService> entity =
                eServiceRepository.checkEServiceStatus(
                        correctEservice,
                        correctDescriptor,
                        correctState).collectList().block();

        Assertions.assertFalse(entity.isEmpty());
        Assertions.assertEquals(entity.get(0), getEntity());
    }


    private EService getEntity(){
        EService entity = new EService();
        entity.setEserviceId(correctEservice);
        entity.setProducerId(correctDescriptor);
        entity.setState("published");
        entity.setTmstInsert(Timestamp.valueOf("2020-01-01 00:00:00"));
        entity.setTmstLastEdit(Timestamp.valueOf("2020-01-01 00:00:00"));
        return entity;
    }



}