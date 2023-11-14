package it.pagopa.interop.signalhub.pull.service.repository;

import it.pagopa.interop.signalhub.pull.service.config.BaseTest;
import it.pagopa.interop.signalhub.pull.service.entities.ConsumerEService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

class ConsumerEServiceRepositoryImplTest extends BaseTest.WithR2DBC {

    private static final String correctEservice = "1234";
    private static final String correctConsumer = "1234";
    private static final String correctState = "PUBLISHED";
    private static final String incorrectState = "ACTIVE";

    @Autowired
    private ConsumerEServiceRepository consumerEServiceRepository;

    @BeforeEach
    void setUp(){
        consumerEServiceRepository.save(getEntity());
    }

    @Test
    void whenFindOrganizationWithBadlyParamThenReturnNull(){
        List<ConsumerEService> entity =
                consumerEServiceRepository.findByConsumerIdAndEServiceIdAndState(
                        correctEservice,
                        correctConsumer,
                        incorrectState).collectList().block();

        Assertions.assertTrue(entity.isEmpty());
    }


    @Test
    void whenFindOrganizationWithCorrectParamThenReturnEntity(){
        List<ConsumerEService> entity =
                consumerEServiceRepository.findByConsumerIdAndEServiceIdAndState(
                        correctEservice,
                        correctConsumer,
                        correctState).collectList().block();

        Assertions.assertFalse(entity.isEmpty());
        Assertions.assertEquals(entity.get(0), getEntity());
    }


    private ConsumerEService getEntity() {
        ConsumerEService entity = new ConsumerEService();
        entity.setEserviceId(correctEservice);
        entity.setConsumerId(correctConsumer);
        entity.setState(correctState);
        entity.setTmstInsert(Timestamp.from(Instant.now()));
        return entity;
    }
}