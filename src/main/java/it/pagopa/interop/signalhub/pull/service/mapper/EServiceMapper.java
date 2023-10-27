package it.pagopa.interop.signalhub.pull.service.mapper;

import it.pagopa.interop.signalhub.pull.service.entities.EService;
import it.pagopa.interop.signalhub.pull.service.repository.cache.model.EServiceCache;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EServiceMapper {

    EService toEntity(EServiceCache fromCache);

    EServiceCache toCache(EService fromEntity);

}
