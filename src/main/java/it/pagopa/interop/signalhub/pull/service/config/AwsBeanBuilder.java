package it.pagopa.interop.signalhub.pull.service.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.auth.credentials.WebIdentityTokenFileCredentialsProvider;
import software.amazon.awssdk.awscore.client.builder.AwsClientBuilder;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kms.KmsClient;


@Configuration
@Slf4j
public class AwsBeanBuilder {

    private final AwsPropertiesConfig props;

    public AwsBeanBuilder(AwsPropertiesConfig props) {
        this.props = props;
    }


    @Bean
    public KmsClient kmsClient() {
        return configureBuilder(KmsClient.builder());
    }


    private <C> C configureBuilder(AwsClientBuilder<?, C> builder) {
        if( props != null ) {

            String profileName = props.getProfile();
            if( StringUtils.isNotBlank( profileName ) ) {
                builder.credentialsProvider( ProfileCredentialsProvider.create( profileName ) );
            } else {
                log.debug("Using WebIdentityTokenFileCredentialsProvider");
                builder.credentialsProvider( WebIdentityTokenFileCredentialsProvider.create() );
            }

            String regionCode = props.getRegion();
            if( StringUtils.isNotBlank( regionCode )) {
                log.debug("Setting region to: {}", regionCode);
                builder.region( Region.of( regionCode ));
            }

        }

        return builder.build();
    }

}