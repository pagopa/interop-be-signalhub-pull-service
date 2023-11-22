package it.pagopa.interop.signalhub.pull.service.repository;

import it.pagopa.interop.signalhub.pull.service.config.BaseTest;
import it.pagopa.interop.signalhub.pull.service.entities.ConsumerEService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

class ConsumerEServiceRepositoryImplTest extends BaseTest.WithR2DBC {

    private static final String correctEservice = "BC-eservice";
    private static final String correctConsumer = "BC-consumer";
    private static final String correctState = "ACTIVE";
    private static final String incorrectState = "ARCHIVED";

    @Autowired
    private ConsumerEServiceRepository consumerEServiceRepository;


    @Test
    void whenFindOrganizationWithBadlyParamThenReturnNull(){
        List<ConsumerEService> entity =
                consumerEServiceRepository.findByConsumerIdAndEServiceIdAndState(
                        correctEservice,
                        correctConsumer,
                        incorrectState).collectList().block();
        Assertions.assertNotNull(entity);
        Assertions.assertTrue(entity.isEmpty());
    }


    @Test
    void whenFindOrganizationWithCorrectParamThenReturnEntity(){
        List<ConsumerEService> entity =
                consumerEServiceRepository.findByConsumerIdAndEServiceIdAndState(
                        correctEservice,
                        correctConsumer,
                        correctState).collectList().block();

        Assertions.assertNotNull(entity);
        Assertions.assertFalse(entity.isEmpty());
        Assertions.assertEquals(entity.get(0), getEntity());
    }


    private ConsumerEService getEntity() {
        ConsumerEService entity = new ConsumerEService();
        entity.setEserviceId(correctEservice);
        entity.setConsumerId(correctConsumer);
        entity.setDescriptorId("BC-descriptor");
        entity.setEventId(12L);
        entity.setAgreementId("BC-agreement");
        entity.setState(correctState);
        return entity;
    }
}