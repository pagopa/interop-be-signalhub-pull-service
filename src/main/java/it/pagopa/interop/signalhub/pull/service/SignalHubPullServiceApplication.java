package it.pagopa.interop.signalhub.pull.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;

@Slf4j
@SpringBootApplication
@EnableR2dbcAuditing
public class SignalHubPullServiceApplication {

	public static void main(String[] args) {

		log.error("AVAILABLE PROCESSOR: {}", Runtime.getRuntime().availableProcessors() );

		SpringApplication.run(SignalHubPullServiceApplication.class, args);
	}

}
