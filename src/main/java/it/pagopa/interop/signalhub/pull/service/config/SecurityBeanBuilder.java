package it.pagopa.interop.signalhub.pull.service.config;

import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import it.pagopa.interop.signalhub.pull.service.auth.JWTAuthManager;
import it.pagopa.interop.signalhub.pull.service.auth.JWTConverter;
import it.pagopa.interop.signalhub.pull.service.auth.PrincipalAgreementValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;

@Configuration
public class SecurityBeanBuilder {

    @Value("${jwk.provider.endpoint}")
    private String jwkProviderEndpoint;

    @Bean
    public JwkProvider jwkProvider(){
        return new JwkProviderBuilder(jwkProviderEndpoint)
                .cached(true)
                .build();
    }


    @Bean
    public JWTConverter getJwtConverter(JwkProvider jwkProvider, SignalHubPullConfig signalHubPullConfig){
        return new JWTConverter(jwkProvider, signalHubPullConfig);
    }

    @Bean
    public PrincipalAgreementValidator getPrincipalAgreementValidator(SignalHubPullConfig signalHubPullConfig){
        return new PrincipalAgreementValidator(signalHubPullConfig);
    }

    @Bean
    public ReactiveAuthenticationManager getReactiveAuthManager() {
        return new JWTAuthManager();
    }



}
