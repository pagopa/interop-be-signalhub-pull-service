package it.pagopa.interop.signalhub.pull.service.config;


import it.pagopa.interop.signalhub.pull.service.auth.PrincipalAgreement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Slf4j
@Configuration
public class BeanBuilder {

    @Bean
    public MockServer getMockServer(@Value("${mockserver.bean.port}") int port){
        log.info("Port :  {}", port);
        return new MockServer(port);
    }

    public static PrincipalAgreement getPrincipal(){
        PrincipalAgreement p = new PrincipalAgreement();
        p.setPrincipalId("ABC-0009");
        p.setState("ACTIVE");
        p.setDescriptorId("VER-001");
        p.setPurposeId("PURPOSE-001");
        p.setProducerId("PAGO-PA-001");
        p.setEServiceId("PN-DIGITAL-001");
        return p;
    }

}
