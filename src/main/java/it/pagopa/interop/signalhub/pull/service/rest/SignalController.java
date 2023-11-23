package it.pagopa.interop.signalhub.pull.service.rest;

import it.pagopa.interop.signalhub.pull.service.config.SignalHubPullConfig;
import it.pagopa.interop.signalhub.pull.service.exception.PDNDGenericException;
import it.pagopa.interop.signalhub.pull.service.rest.v1.api.GatewayApi;
import it.pagopa.interop.signalhub.pull.service.rest.v1.dto.PaginationSignal;
import it.pagopa.interop.signalhub.pull.service.rest.v1.dto.Signal;
import it.pagopa.interop.signalhub.pull.service.service.SignalService;
import it.pagopa.interop.signalhub.pull.service.utils.Utility;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

import static it.pagopa.interop.signalhub.pull.service.exception.ExceptionTypeEnum.NO_AUTH_FOUNDED;


@RestController
@AllArgsConstructor
public class SignalController implements GatewayApi {
    private SignalService signalService;
    private SignalHubPullConfig signalHubPullConfig;


    @Override
    public Mono<ResponseEntity<PaginationSignal>> getRequest(String eserviceId, Long signalId, Long size, ServerWebExchange exchange) {

        final Long finalSize = (size == 0 || size > signalHubPullConfig.getMaxNumberPage()) ? signalHubPullConfig.getMaxNumberPage() : size;

        return Utility.getPrincipalFromSecurityContext()
                .switchIfEmpty(Mono.error(new PDNDGenericException(NO_AUTH_FOUNDED, NO_AUTH_FOUNDED.getMessage(), HttpStatus.UNAUTHORIZED)))
                .zipWhen(principalAgreement -> this.signalService.counter(eserviceId))
                .flatMap(principalAndCounter -> {
                    var principal= principalAndCounter.getT1();
                    var finalStatus = finalSize < principalAndCounter.getT2() ? HttpStatus.PARTIAL_CONTENT : HttpStatus.OK;
                    return this.signalService.pullSignal(principal.getPrincipalId(), eserviceId, signalId, finalSize)
                            .collectList()
                            .map(list -> ResponseEntity.status(finalStatus)
                                    .body(toPagination(list)));
                });
    }


    private PaginationSignal toPagination(List<Signal> list) {
        PaginationSignal paginationSignal = new PaginationSignal();
        paginationSignal.setSignals(list);
        if (list == null || list.isEmpty()) paginationSignal.setLastSignalId(null);
        else {
            paginationSignal.setLastSignalId(list.get(list.size()-1).getSignalId());
        }
        return paginationSignal;
    }



}
