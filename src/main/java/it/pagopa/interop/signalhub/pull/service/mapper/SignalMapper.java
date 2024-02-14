package it.pagopa.interop.signalhub.pull.service.mapper;

import it.pagopa.interop.signalhub.pull.service.entities.SignalEntity;
import it.pagopa.interop.signalhub.pull.service.rest.v1.dto.Signal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SignalMapper {

    Signal toDto(SignalEntity signal);

    SignalEntity toEntity(Signal dto);
}
