package it.pagopa.interop.signalhub.pull.service.rest;

import it.pagopa.interop.signalhub.pull.service.rest.v1.api.GatewayApi;
import it.pagopa.interop.signalhub.pull.service.rest.v1.dto.Signal;
import it.pagopa.interop.signalhub.pull.service.service.SignalService;
import it.pagopa.interop.signalhub.pull.service.utils.Const;
import it.pagopa.interop.signalhub.pull.service.utils.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
public class SignalController implements GatewayApi {


    @Autowired
    private SignalService signalService;



    public Mono<ResponseEntity<Flux<Signal>>> pullSignal(String eServiceId, Long indexSignal,  final ServerWebExchange exchange) {
        return Utility.getFromContext(Const.ORGANIZATION_ID_VALUE, null)
                .switchIfEmpty(Mono.empty())
                .flatMap(organizationAndSignalRequest -> {
                    return this.signalService.pullSignal(organizationAndSignalRequest, eServiceId, indexSignal);
                }).map(signalResponse -> ResponseEntity.status(HttpStatus.OK).body(signalResponse));

    }










}
