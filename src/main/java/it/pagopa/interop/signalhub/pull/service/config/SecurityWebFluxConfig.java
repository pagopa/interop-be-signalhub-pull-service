package it.pagopa.interop.signalhub.pull.service.config;


import it.pagopa.interop.signalhub.pull.service.filter.JWTFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Profile("!test")
@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityWebFluxConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http, JWTFilter jwtFilter) {

        http.csrf(ServerHttpSecurity.CsrfSpec::disable);

        http.authorizeExchange(authReq -> {
            authReq.pathMatchers("/actuator/health/**").permitAll();
            authReq.anyExchange().authenticated();
        });

        http.addFilterAt(jwtFilter, SecurityWebFiltersOrder.FIRST);

        return http.build();
    }


}
