package it.pagopa.interop.signalhub.pull.service.mapper;

import it.pagopa.interop.signalhub.pull.service.auth.PrincipalAgreement;
import it.pagopa.interop.signalhub.pull.service.generated.openapi.client.interop.model.v1.Agreement;
import it.pagopa.interop.signalhub.pull.service.generated.openapi.client.interop.model.v1.AgreementState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;


class PrincipalAgreementMapperTest {
    private final PrincipalAgreementMapper principalAgreementMapper = Mappers.getMapper(PrincipalAgreementMapper.class);
    private String principalId;
    private String purposeId;
    private String eServiceId;
    private String producerId;
    private String descriptorId;
    private AgreementState state;

    @BeforeEach
    void preTest() {
        setUp();
    }

    @Test
    void toPrincipal() {
        Agreement agreement = getAgreement();
        PrincipalAgreement principalAgreement = principalAgreementMapper.toPrincipal(agreement);
        assertNotNull(principalAgreement);
        assertEquals(agreement.getEserviceId().toString(), principalAgreement.getEServiceId());
        assertEquals(agreement.getId().toString(), principalAgreement.getPurposeId());
        assertEquals(agreement.getConsumerId().toString(), principalAgreement.getPrincipalId());
        assertEquals(agreement.getProducerId().toString(), principalAgreement.getProducerId());
        assertEquals(agreement.getDescriptorId().toString(), principalAgreement.getDescriptorId());

        agreement.setId(null);
        principalAgreement = principalAgreementMapper.toPrincipal(agreement);
        assertNull(principalAgreement.getPurposeId());

        agreement.setConsumerId(null);
        principalAgreement = principalAgreementMapper.toPrincipal(agreement);
        assertNull(principalAgreement.getPrincipalId());

        agreement.setEserviceId(null);
        principalAgreement = principalAgreementMapper.toPrincipal(agreement);
        assertNull(principalAgreement.getEServiceId());

        agreement.setProducerId(null);
        principalAgreement = principalAgreementMapper.toPrincipal(agreement);
        assertNull(principalAgreement.getProducerId());

        agreement.setDescriptorId(null);
        principalAgreement = principalAgreementMapper.toPrincipal(agreement);
        assertNull(principalAgreement.getDescriptorId());

        agreement.setState(null);
        principalAgreement = principalAgreementMapper.toPrincipal(agreement);
        assertNull(principalAgreement.getState());
    }

    @Test
    void whenCallToPrincipalAndAgreementIsNull() {
        Agreement agreement = null;
        PrincipalAgreement principalAgreement = principalAgreementMapper.toPrincipal(agreement);
        assertNull(principalAgreement);
    }

    private Agreement getAgreement() {
        Agreement agreement = new Agreement();
        agreement.setId(UUID.fromString(principalId));
        agreement.setEserviceId(UUID.fromString(purposeId));
        agreement.setDescriptorId(UUID.fromString(eServiceId));
        agreement.setProducerId(UUID.fromString(producerId));
        agreement.setConsumerId(UUID.fromString(descriptorId));
        agreement.setState(state);
        return agreement;
    }

    private void setUp() {
        principalId = "0f14d0ab-9605-4a62-a9e4-5ed26688389b";
        purposeId = "4a620f14-d0ab-9605-a9e4-5ed26688389b";
        eServiceId = "5ed26688-389b-4a62-9605-0f14d0ab9605";
        producerId = "8a631cab-797b-482d-bf7d-615bf1004368";
        descriptorId = "01920f14-d0ab-9605-a9e4-374650192842";
        state = AgreementState.ARCHIVED;
    }
}
