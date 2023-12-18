package it.pagopa.interop.signalhub.pull.service.mapper;


import it.pagopa.interop.signalhub.pull.service.auth.PrincipalAgreement;
import it.pagopa.interop.signalhub.pull.service.generated.openapi.client.interop.model.v1.Agreement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PrincipalAgreementMapper {

    @Mapping(target = "purposeId", source= "agreement.id")
    @Mapping(target = "principalId", source= "agreement.consumerId")
    @Mapping(target = "EServiceId", source= "agreement.eserviceId")
    PrincipalAgreement toPrincipal(Agreement agreement);


}
