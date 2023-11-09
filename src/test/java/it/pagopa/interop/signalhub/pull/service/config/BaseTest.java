package it.pagopa.interop.signalhub.pull.service.config;


import it.pagopa.interop.signalhub.pull.service.LocalStackTestConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest()
@ActiveProfiles("test")
@Import(LocalStackTestConfig.class)
public class BaseTest {



    @ActiveProfiles("test")
    @Import(LocalStackTestConfig.class)
    @SpringBootTest()
    @AutoConfigureWebTestClient
    public static class WithWebEnvironment{
        protected static final String TOKEN_OK = "Bearer eyJ0eXAiOiJhdCtqd3QiLCJhbGciOiJSUzI1NiIsInVzZSI6InNpZyIsImtpZCI6IjMyZDhhMzIxLTE1NjgtNDRmNS05NTU4LWE5MDcyZjUxOWQyZCJ9.eyJvcmdhbml6YXRpb25JZCI6Ijg0ODcxZmQ0LTJmZDctNDZhYi05ZDIyLWY2YjQ1MmY0YjNjNSIsImF1ZCI6InVhdC5pbnRlcm9wLnBhZ29wYS5pdC9tMm0iLCJzdWIiOiIxMTM2ODUyNy00OTRiLTQyNDQtOTcwNi1iNzgyYjU0NDM4MzEiLCJyb2xlIjoibTJtIiwibmJmIjoxNjk1NzE3NDY4LCJpc3MiOiJ1YXQuaW50ZXJvcC5wYWdvcGEuaXQiLCJleHAiOjE2OTU3MTgwNjgsImlhdCI6MTY5NTcxNzQ2OCwiY2xpZW50X2lkIjoiMTEzNjg1MjctNDk0Yi00MjQ0LTk3MDYtYjc4MmI1NDQzODMxIiwianRpIjoiZTlhOGNkNDMtOWNlYy00OWU5LWIzNjgtMDJmNmYzYTRmMmExIn0.flU9M2b3aMYD2Z7nHrSGCiDnNJ3KXyOorqrQVJdp8R3KX-BXdfnG6NIz_WRBtC2OU-Ftl3grrpgcGHbUXm5pWteXUaA6OAUxEBsFdwRc9nG1Pcqs1nvpWM4ZMC8MZK9W6LKMixoepyhseLIxej8KSL8qBCmvp-dhzPvegJpe4RZF9Ku0zsz0TAsVxHcMOQ36v9CzZtAbmcZ-lrE-4qzSI40nGzANRKAPiGPW1FhEpskvZfVu2K7HAajX7npQ8gcZQRCKGxUiVDRevWZwPbokd8LLyhTvXTGsXmfqzcNss2n9f4G1AdsedS57ouc8QF-69QRNz-TxVdDD_8i1zlfVkQ";


    }

    @Slf4j
    @SpringBootTest
    @EnableAutoConfiguration
    @ActiveProfiles("test")
    public static class WithMockServer {
        @Autowired
        private MockServer mockServer;


        @BeforeEach
        public void init(){
            log.info(this.getClass().getSimpleName());
            //TODO set name file with name class + ".json";
            setExpection(this.getClass().getSimpleName() + "-webhook.json");
        }

        @AfterEach
        public void kill(){
            log.info("Killed");
            this.mockServer.stop();
        }

        public void setExpection(String file){
            this.mockServer.initializationExpection(file);
        }
    }



}
