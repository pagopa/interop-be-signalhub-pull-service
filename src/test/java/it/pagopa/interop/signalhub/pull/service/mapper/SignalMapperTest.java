package it.pagopa.interop.signalhub.pull.service.mapper;

import it.pagopa.interop.signalhub.pull.service.entities.SignalEntity;
import it.pagopa.interop.signalhub.pull.service.rest.v1.dto.Signal;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
class SignalMapperTest {


    private SignalMapper service = Mappers.getMapper(SignalMapper.class);

    @Test
    void toDto() {
        SignalEntity signal1= new SignalEntity();
        signal1.setSignalId(1L);
        Signal signal= service.toDto(signal1);
        assertEquals(signal.getIndexSignal().toString(), signal.getSignalId() );
    }

    @Test
    void toEntity() {
        Signal signal= new Signal();
        signal.setIndexSignal(1l);
        SignalEntity signalEntity= service.toEntity(signal);
        assertEquals(signal.getIndexSignal(), signalEntity.getSignalId());
    }
}