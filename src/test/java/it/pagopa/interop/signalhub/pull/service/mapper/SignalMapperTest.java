package it.pagopa.interop.signalhub.pull.service.mapper;

import it.pagopa.interop.signalhub.pull.service.entities.SignalEntity;
import it.pagopa.interop.signalhub.pull.service.rest.v1.dto.Signal;
import it.pagopa.interop.signalhub.pull.service.rest.v1.dto.SignalType;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mapstruct.factory.Mappers;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


@RunWith(SpringRunner.class)
class SignalMapperTest {
    private final SignalMapper service = Mappers.getMapper(SignalMapper.class);

    @Test
    void toDto() {
        SignalEntity signalEntity = new SignalEntity();
        signalEntity.setSignalId(1L);
        Signal signal = service.toDto(signalEntity);
        assertEquals(signalEntity.getSignalId(), signal.getSignalId());

        signalEntity.setSignalType(SignalType.CREATE.toString());
        signal = service.toDto(signalEntity);
        assertEquals(signalEntity.getSignalType(), signal.getSignalType().toString());
    }

    @Test
    void whenCallToDtoAndSignalEntityIsNull() {
        SignalEntity signalEntity = null;
        Signal signal = service.toDto(signalEntity);
        assertNull(signal);
    }

    @Test
    void toEntity() {
        Signal signal = new Signal();
        signal.setSignalId(1L);
        SignalEntity signalEntity = service.toEntity(signal);
        assertEquals(signal.getSignalId(), signalEntity.getSignalId());

        signal.setSignalType(SignalType.CREATE);
        signalEntity = service.toEntity(signal);
        assertEquals(signal.getSignalType().toString(), signalEntity.getSignalType());
    }

    @Test
    void whenCallToEntityAndSignalIsNull() {
        Signal signal = null;
        SignalEntity signalEntity = service.toEntity(signal);
        assertNull(signalEntity);
    }
}