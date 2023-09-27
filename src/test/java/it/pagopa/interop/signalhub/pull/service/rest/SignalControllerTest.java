package it.pagopa.interop.signalhub.pull.service.rest;

import it.pagopa.interop.signalhub.pull.service.LocalStackTestConfig;
import it.pagopa.interop.signalhub.pull.service.rest.v1.dto.Signal;
import it.pagopa.interop.signalhub.pull.service.service.SignalService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;


@Import(LocalStackTestConfig.class)
@ActiveProfiles("test")
@WebFluxTest(controllers = {SignalController.class})
class SignalControllerTest {

    @Autowired
    private WebTestClient webTestClient;
    @MockBean
    private SignalService signalService;

    @Test
    void pushSignal() {
        Signal signalResponse= new Signal();
        String path = "/pull-signal";
        Mockito.when(signalService.pullSignal(Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn(Flux.just(new Signal()));

        webTestClient.post()
                .uri(uriBuilder -> uriBuilder.path(path)
                        .build())
                .exchange()
                .expectStatus().isOk();
    }

}

