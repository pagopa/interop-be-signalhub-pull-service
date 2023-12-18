package it.pagopa.interop.signalhub.pull.service.externalclient;


import it.pagopa.interop.signalhub.pull.service.config.BaseTest;
import it.pagopa.interop.signalhub.pull.service.generated.openapi.client.interop.model.v1.Agreement;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import static org.junit.jupiter.api.Assertions.*;


class InteroperabilityClientImplTest extends BaseTest.WithMockServer {

    private static final String PURPOSE_BAD_REQUEST = "498457c9-f4de-40d2-ad84-aed23c3584fd";
    private static final String PURPOSE_OK = "9a7e5371-0832-4301-9d97-d762f703dd78";

    @Autowired
    private InteroperabilityClient interoperabilityClient;



    @Test
    void whenRetrieveByPurposeIdThenReturnPrincipalAgreementDetail(){
        Agreement agreement = interoperabilityClient.getAgreementByPurposeId(PURPOSE_OK).block();
        assertNotNull(agreement);
        assertEquals(PURPOSE_OK, agreement.getId().toString());
    }

    @Test
    void whenPurposeIdBadlyFormatThenThrowBadRequestException() {
        WebClientResponseException exception =
                assertThrows(WebClientResponseException.class, () -> interoperabilityClient.getAgreementByPurposeId(PURPOSE_BAD_REQUEST).block());

        assertNotNull(exception);
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
    }


}
