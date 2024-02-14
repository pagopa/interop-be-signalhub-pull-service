package it.pagopa.interop.signalhub.pull.service.config;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Configuration
public class SecurityTestConfig {

    @Bean
    @Qualifier("interop-webclient")
    public WebClient getWebClient(){
        return WebClient.builder().build();
    }


    @Bean
    public WithSecurityContextFactory<WithMockCustomUser> withMockCustomUserWithSecurityContextFactory(){
        return new WithMockCustomUserSecurityContextFactory();
    }

    public static class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

        @Override
        public SecurityContext createSecurityContext(WithMockCustomUser withMockCustomUser) {
            SecurityContext context = SecurityContextHolder.createEmptyContext();

            Authentication auth = new UsernamePasswordAuthenticationToken(BeanBuilder.getPrincipal(), null, List.of(new SimpleGrantedAuthority("ORGANIZATION")));
            context.setAuthentication(auth);
            return context;
        }
    }

}
