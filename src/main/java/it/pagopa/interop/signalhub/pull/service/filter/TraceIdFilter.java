package it.pagopa.interop.signalhub.pull.service.filter;

import it.pagopa.interop.signalhub.pull.service.config.SignalHubPullConfig;
import lombok.AllArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.Collections;
import java.util.UUID;

import static it.pagopa.interop.signalhub.pull.service.utils.Const.TRACE_ID_KEY;


@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@AllArgsConstructor
public class TraceIdFilter implements WebFilter {
    private final SignalHubPullConfig cfg;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        HttpHeaders headers = exchange.getRequest().getHeaders();
        var traceId = UUID.randomUUID().toString();
        if (headers.containsKey(cfg.getHeaderTraceIdKey())) {
            traceId = headers.getFirst(cfg.getHeaderTraceIdKey());
        }
        exchange.getResponse().getHeaders()
                .putIfAbsent(cfg.getHeaderTraceIdKey(), Collections.singletonList(traceId));


        String finalTraceId = traceId;
        return chain.filter(exchange).contextWrite(Context.of(TRACE_ID_KEY, finalTraceId));
    }
}
