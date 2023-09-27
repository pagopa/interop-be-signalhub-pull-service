package it.pagopa.interop.signalhub.pull.service.mapper;

import it.pagopa.interop.signalhub.pull.service.entities.SignalEntity;
import it.pagopa.interop.signalhub.pull.service.rest.v1.dto.Signal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SignalMapper {
    @Mapping(target = "indexSignal", source = "signal.signalId")
    Signal toDto(SignalEntity signal);

    @Mapping(target = "signalId", source = "dto.indexSignal")
    SignalEntity toEntity(Signal dto);
}
