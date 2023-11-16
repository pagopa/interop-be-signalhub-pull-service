package it.pagopa.interop.signalhub.pull.service.rest;

import it.pagopa.interop.signalhub.pull.service.config.SignalHubPullConfig;
import it.pagopa.interop.signalhub.pull.service.exception.PDNDGenericException;
import it.pagopa.interop.signalhub.pull.service.rest.v1.api.GatewayApi;
import it.pagopa.interop.signalhub.pull.service.rest.v1.dto.PaginationSignal;
import it.pagopa.interop.signalhub.pull.service.rest.v1.dto.Signal;
import it.pagopa.interop.signalhub.pull.service.service.SignalService;
import it.pagopa.interop.signalhub.pull.service.utils.Utility;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static it.pagopa.interop.signalhub.pull.service.exception.ExceptionTypeEnum.NO_AUTH_FOUNDED;


@RestController
@AllArgsConstructor
public class SignalController implements GatewayApi {
    private SignalService signalService;

    @Autowired
    private SignalHubPullConfig signalHubPullConfig;

    @Override
    public Mono<ResponseEntity<PaginationSignal>> getRequest(String eserviceId, Long indexSignal, Long size, ServerWebExchange exchange) {



        return Utility.getPrincipalFromSecurityContext()
                .switchIfEmpty(Mono.error(new PDNDGenericException(NO_AUTH_FOUNDED, NO_AUTH_FOUNDED.getMessage(), HttpStatus.UNAUTHORIZED)))
                .flatMapMany(principalAgreement -> {
                    return this.signalService.pullSignal(principalAgreement.getPrincipalId(), eserviceId, indexSignal,
                            size == 0 || size > signalHubPullConfig.getMaxNumberPage() ? signalHubPullConfig.getMaxNumberPage() : size);
                })
                .collectList()
                .map(list -> {
                    PaginationSignal paginationSignal = new PaginationSignal();
                    paginationSignal.setSignals(list);
                    paginationSignal.setLastIndexSignal(list.get(list.size()-1).getIndexSignal());
                    return ResponseEntity.status(size > signalHubPullConfig.getMaxNumberPage() ? HttpStatus.PARTIAL_CONTENT : HttpStatus.OK)
                            .body(paginationSignal);
                });
    }










}
