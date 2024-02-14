package it.pagopa.interop.signalhub.pull.service.mapper;

import it.pagopa.interop.signalhub.pull.service.entities.ConsumerEService;
import it.pagopa.interop.signalhub.pull.service.cache.model.ConsumerEServiceCache;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ConsumerEServiceMapper {

    ConsumerEService toEntity(ConsumerEServiceCache fromCache);

    ConsumerEServiceCache toCache(ConsumerEService fromEntity);

}
