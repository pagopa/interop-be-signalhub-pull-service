//package it.pagopa.interop.signalhub.pull.service.rest;
//
//import it.pagopa.interop.signalhub.pull.service.LocalStackTestConfig;
//import it.pagopa.interop.signalhub.pull.service.config.BaseTest;
//import it.pagopa.interop.signalhub.pull.service.config.WithMockCustomUser;
//import it.pagopa.interop.signalhub.pull.service.rest.v1.dto.Signal;
//import it.pagopa.interop.signalhub.pull.service.service.SignalService;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.Import;
//import org.springframework.http.HttpHeaders;
//import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.reactive.server.WebTestClient;
//import reactor.core.publisher.Flux;
//
//
//
//class SignalControllerTest extends BaseTest.WithWebEnvironment {
//
//    @Autowired
//    private WebTestClient webTestClient;
//    @MockBean
//    private SignalService signalService;
//
//    @Test
//    @WithMockCustomUser
//    void pullSignal() {
//        String path = "/pull-signal/123";
//        Mockito.when(signalService.pullSignal(Mockito.any(), Mockito.any(), Mockito.any()))
//                .thenReturn(Flux.just(new Signal()));
//
//        webTestClient.mutateWith(SecurityMockServerConfigurers.csrf())
//                .get()
//                .uri(uriBuilder -> uriBuilder.path(path)
//                        .build())
//                .header(HttpHeaders.AUTHORIZATION, TOKEN_OK)
//                .exchange()
//                .expectStatus().isOk();
//    }
//
//}
//
