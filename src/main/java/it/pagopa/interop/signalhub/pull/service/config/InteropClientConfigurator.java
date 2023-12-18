package it.pagopa.interop.signalhub.pull.service.config;


import it.pagopa.interop.signalhub.pull.service.generated.openapi.client.interop.api.ApiClient;
import it.pagopa.interop.signalhub.pull.service.generated.openapi.client.interop.api.v1.GatewayApi;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class InteropClientConfigurator {
    @Value("${pdnd.client.event.endpoint-url}")
    private String basePath;

    @Bean
    public GatewayApi getRecipientsApiDataVault(@Qualifier("interop-webclient") WebClient webClient){
        ApiClient apiClient = new ApiClient(webClient);
        apiClient.setBasePath(basePath);
        return new GatewayApi(apiClient);
    }

}
