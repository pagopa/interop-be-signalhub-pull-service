package it.pagopa.interop.signalhub.pull.service.rest;

import it.pagopa.interop.signalhub.pull.service.exception.PDNDGenericException;
import it.pagopa.interop.signalhub.pull.service.rest.v1.api.GatewayApi;
import it.pagopa.interop.signalhub.pull.service.rest.v1.dto.Signal;
import it.pagopa.interop.signalhub.pull.service.service.SignalService;
import it.pagopa.interop.signalhub.pull.service.utils.Utility;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static it.pagopa.interop.signalhub.pull.service.exception.ExceptionTypeEnum.NO_AUTH_FOUNDED;


@RestController
@AllArgsConstructor
public class SignalController implements GatewayApi {
    private SignalService signalService;

    @Override
    public Mono<ResponseEntity<Flux<Signal>>> getRequest(String eserviceId, java.lang.Long indexSignal, ServerWebExchange exchange) {
        return Utility.getPrincipalFromSecurityContext()
                .switchIfEmpty(Mono.error(new PDNDGenericException(NO_AUTH_FOUNDED, NO_AUTH_FOUNDED.getMessage(), HttpStatus.UNAUTHORIZED)))
                .map(consumerId ->
                        ResponseEntity.status(HttpStatus.OK)
                                .body(this.signalService.pullSignal(consumerId, eserviceId, indexSignal))
                );
    }










}
