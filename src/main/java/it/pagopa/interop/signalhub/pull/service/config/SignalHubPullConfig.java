package it.pagopa.interop.signalhub.pull.service.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "application.eservice.pull")
public class SignalHubPullConfig {
    private String id;
    private String audience;
}
