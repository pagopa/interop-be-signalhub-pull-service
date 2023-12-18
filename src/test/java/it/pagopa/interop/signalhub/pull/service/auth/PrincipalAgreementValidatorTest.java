package it.pagopa.interop.signalhub.pull.service.auth;

import it.pagopa.interop.signalhub.pull.service.config.SignalHubPullConfig;
import it.pagopa.interop.signalhub.pull.service.generated.openapi.client.interop.model.v1.AgreementState;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class PrincipalAgreementValidatorTest {

    @Mock
    private SignalHubPullConfig signalHubPullConfig;

    @InjectMocks
    private PrincipalAgreementValidator principalAgreementValidator;

    @Test
    void test() {
        PrincipalAgreement principalAgreement= new PrincipalAgreement();
        principalAgreement.setEServiceId(signalHubPullConfig.getId());
        principalAgreement.setState(AgreementState.ACTIVE.getValue());
        assertTrue(principalAgreementValidator.test(principalAgreement));
    }
}