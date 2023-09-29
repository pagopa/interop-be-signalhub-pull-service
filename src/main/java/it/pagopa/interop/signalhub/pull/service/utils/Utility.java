package it.pagopa.interop.signalhub.pull.service.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import reactor.core.publisher.Mono;

@Slf4j
public class Utility {

    private Utility(){
        // private constructor
    }

    public static Mono<String> getPrincipalFromSecurityContext(){
        return ReactiveSecurityContextHolder.getContext()
                .map(securityContext -> (String) securityContext.getAuthentication().getPrincipal());
    }

}
