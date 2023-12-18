package it.pagopa.interop.signalhub.pull.service.filter;


import it.pagopa.interop.signalhub.pull.service.auth.JWTConverter;
import it.pagopa.interop.signalhub.pull.service.auth.JWTUtil;
import it.pagopa.interop.signalhub.pull.service.auth.PrincipalAgreement;
import it.pagopa.interop.signalhub.pull.service.auth.PrincipalAgreementValidator;
import it.pagopa.interop.signalhub.pull.service.exception.JWTException;
import it.pagopa.interop.signalhub.pull.service.exception.PDNDGenericException;
import it.pagopa.interop.signalhub.pull.service.cache.model.JWTCache;
import it.pagopa.interop.signalhub.pull.service.service.InteropService;
import it.pagopa.interop.signalhub.pull.service.service.JWTService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

import static it.pagopa.interop.signalhub.pull.service.exception.ExceptionTypeEnum.AGREEMENT_NOT_VALID;

@Profile("!test")
@Slf4j
@AllArgsConstructor
@Configuration
public class JWTFilter implements WebFilter {
    private final JWTConverter jwtConverter;
    private final PrincipalAgreementValidator principalAgreementValidator;
    private final ReactiveAuthenticationManager reactiveAuthManager;
    private final ServerSecurityContextRepository securityContextRepository = NoOpServerSecurityContextRepository.getInstance();
    private final ServerAuthenticationSuccessHandler authSuccessHandler;
    private final JWTService jwtService;
    private final InteropService interopService;



    @Override
    public Mono<Void> filter(@NonNull ServerWebExchange exchangeRequest, @NonNull WebFilterChain chain) {
        if (exchangeRequest.getRequest().getHeaders().get("Authorization") == null) {
            return chain.filter(exchangeRequest);
        }

        return Mono.justOrEmpty(exchangeRequest)
                .doOnNext(exchange -> log.info("Start JWT filter validation"))
                .flatMap(jwtConverter)
                .doOnNext(jwt -> log.info("JWT decoded"))
                .switchIfEmpty(chain.filter(exchangeRequest).then(Mono.empty()))
                .doOnNext(jwt -> log.info("JWT is valid ?"))
                .flatMap(jwtService::findByJWT)
                .doOnNext(jwt -> log.info("JWT is valid"))
                .map(JWTUtil::getPurposeClaim)
                .flatMap(interopService::getPrincipalFromPurposeId)
                .filter(principalAgreementValidator)
                .switchIfEmpty(Mono.error(new PDNDGenericException(AGREEMENT_NOT_VALID, AGREEMENT_NOT_VALID.getMessage(), HttpStatus.FORBIDDEN)))
                .doOnNext(principal -> log.info("Principal is valid"))
                .flatMap(principal -> authenticate(exchangeRequest, chain, principal))
                .onErrorResume(JWTException.class, ex ->
                        jwtService.saveOnCache(new JWTCache(ex.getJwt()))
                                .flatMap(item -> Mono.error(new PDNDGenericException(ex.getExceptionType(), ex.getMessage(), ex.getHttpStatus())))
                );


    }

    private Mono<Void> authenticate(ServerWebExchange exchange, WebFilterChain chain, PrincipalAgreement principalAgreement) {
        WebFilterExchange webFilterExchange = new WebFilterExchange(exchange, chain);
        return this.reactiveAuthManager.authenticate(getAuthentication(principalAgreement))
                .flatMap(authentication -> onAuthSuccess(authentication, webFilterExchange));
    }

    private Mono<Void> onAuthSuccess(Authentication authentication, WebFilterExchange webFilterExchange) {
        ServerWebExchange exchange = webFilterExchange.getExchange();
        SecurityContextImpl securityContext = new SecurityContextImpl();
        securityContext.setAuthentication(authentication);
        return this.securityContextRepository.save(exchange, securityContext)
                .then(this.authSuccessHandler.onAuthenticationSuccess(webFilterExchange, authentication))
                .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)));
    }


    public static Authentication getAuthentication(PrincipalAgreement principalAgreement) {
        return new UsernamePasswordAuthenticationToken(principalAgreement, null, List.of(new SimpleGrantedAuthority("ORGANIZATION")));
    }


}

