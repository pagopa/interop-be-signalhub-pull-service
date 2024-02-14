package it.pagopa.interop.signalhub.pull.service.config;


import it.pagopa.interop.signalhub.pull.service.logging.ContextLifter;
import it.pagopa.interop.signalhub.pull.service.logging.MdcContextLifter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Operators;

@Configuration
public class ReactorConfiguration {


    @Bean
    public void contextLifterConfiguration() {
        Hooks.onEachOperator(MdcContextLifter.class.getSimpleName(),
                Operators.lift((sc, sub) -> new ContextLifter<>(sub)));
    }
}
