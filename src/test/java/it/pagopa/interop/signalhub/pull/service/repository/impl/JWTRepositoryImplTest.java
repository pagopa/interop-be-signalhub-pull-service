package it.pagopa.interop.signalhub.pull.service.repository.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import it.pagopa.interop.signalhub.pull.service.exception.ExceptionTypeEnum;
import it.pagopa.interop.signalhub.pull.service.exception.PDNDGenericException;
import it.pagopa.interop.signalhub.pull.service.repository.cache.model.JWTCache;
import it.pagopa.interop.signalhub.pull.service.repository.cache.repository.JWTCacheRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JWTRepositoryImplTest {

    @InjectMocks
    private JWTRepositoryImpl jwtRepository;

    @Mock
    private JWTCacheRepository cacheRepository;

    @Test
    void findByJWTButNotFoundInCache() {
        DecodedJWT jwt = JWT.decode("eyJhbGciOiJIUzI1NiJ9.eyJvYmplY3QiOnsibmFtZSI6ImpvaG4ifX0.lrU1gZlOdlmTTeZwq0VI-pZx2iV46UWYd5-lCjy6-c4");
        Mockito.when(cacheRepository.findById(Mockito.any())).thenReturn(Mono.empty());
        assertNotNull(jwtRepository.findByJWT(jwt).block());
    }

    @Test
    void findByJWTButAnsFoundedInCache() {
        DecodedJWT jwt = JWT.decode("eyJhbGciOiJIUzI1NiJ9.eyJvYmplY3QiOnsibmFtZSI6ImpvaG4ifX0.lrU1gZlOdlmTTeZwq0VI-pZx2iV46UWYd5-lCjy6-c4");
        Mockito.when(cacheRepository.findById(Mockito.any())).thenReturn(Mono.just(new JWTCache("123")));

        StepVerifier.create(jwtRepository.findByJWT(jwt))
                .expectErrorMatches((ex) -> {
                    assertTrue(ex instanceof PDNDGenericException);
                    assertEquals(ExceptionTypeEnum.JWT_NOT_VALID, ((PDNDGenericException) ex).getExceptionType());
                    return true;
                }).verify();
    }

    @Test
    void saveOnCache() {
        Mockito.when(cacheRepository.save(Mockito.any())).thenReturn(Mono.just(new JWTCache()));
        assertNotNull(jwtRepository.saveOnCache(new JWTCache()).block());
    }



}