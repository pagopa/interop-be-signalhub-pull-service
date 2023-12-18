package it.pagopa.interop.signalhub.pull.service.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.pagopa.interop.signalhub.pull.service.rest.v1.dto.Problem;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;


@Slf4j
@Configuration
@AllArgsConstructor
public class PDNDErrorWebExceptionHandler implements ErrorWebExceptionHandler {
    private PDNDExceptionHelper helper;
    private ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(@NonNull ServerWebExchange serverWebExchange, @NonNull Throwable throwable) {
        DataBufferFactory bufferFactory = serverWebExchange.getResponse().bufferFactory();
        DataBuffer dataBuffer;
        log.error("Error Internal : {}", throwable.getMessage(), throwable);
        try {
            Problem problem = this.helper.handle(throwable);
            serverWebExchange.getResponse().setStatusCode(HttpStatus.resolve(problem.getStatus()));
            dataBuffer = bufferFactory.wrap(this.objectMapper.writeValueAsBytes(problem));
        } catch (Exception ex) {
            log.error("cannot output problem", ex);
            dataBuffer = bufferFactory.wrap(this.helper.generateFallbackProblem().getBytes());
        }
        serverWebExchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return serverWebExchange.getResponse().writeWith(Mono.just(dataBuffer));
    }
}
