package it.pagopa.interop.signalhub.pull.service.config;

import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityBeanBuilder {

    @Value("${jwk.provider.endpoint}")
    private String jwkProviderEndpoint;

    @Bean
    public JwkProvider jwkProvider(){
        return new JwkProviderBuilder(jwkProviderEndpoint)
                .build();
    }


}
