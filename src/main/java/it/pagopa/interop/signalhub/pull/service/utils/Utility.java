package it.pagopa.interop.signalhub.pull.service.utils;

import it.pagopa.interop.signalhub.pull.service.auth.PrincipalAgreement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import reactor.core.publisher.Mono;

@Slf4j
public class Utility {

    private Utility(){
        // private constructor
    }

    public static Mono<PrincipalAgreement> getPrincipalFromSecurityContext(){
        return ReactiveSecurityContextHolder.getContext()
                .map(securityContext -> (PrincipalAgreement) securityContext.getAuthentication().getPrincipal());
    }

}
