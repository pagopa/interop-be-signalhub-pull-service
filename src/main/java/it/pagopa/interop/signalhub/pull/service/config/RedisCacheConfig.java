package it.pagopa.interop.signalhub.pull.service.config;

import it.pagopa.interop.signalhub.pull.service.repository.cache.model.ConsumerEServiceCache;
import it.pagopa.interop.signalhub.pull.service.repository.cache.model.JWTCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@Slf4j
public class RedisCacheConfig {


    @Bean
    public ReactiveRedisTemplate<String, ConsumerEServiceCache> reactiveRedisTemplate(ReactiveRedisConnectionFactory factory) {
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<ConsumerEServiceCache> valueSerializer =
                new Jackson2JsonRedisSerializer<>(ConsumerEServiceCache.class);
        RedisSerializationContext.RedisSerializationContextBuilder<String, ConsumerEServiceCache> builder =
                RedisSerializationContext.newSerializationContext(keySerializer);
        RedisSerializationContext<String, ConsumerEServiceCache> context =
                builder.value(valueSerializer).build();
       return new ReactiveRedisTemplate<>(factory, context);
    }

    @Bean
    public ReactiveRedisTemplate<String, JWTCache> reactiveRedisTemplateJWT(ReactiveRedisConnectionFactory factory) {
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<JWTCache> valueSerializer =
                new Jackson2JsonRedisSerializer<>(JWTCache.class);
        RedisSerializationContext.RedisSerializationContextBuilder<String, JWTCache> builder =
                RedisSerializationContext.newSerializationContext(keySerializer);
        RedisSerializationContext<String, JWTCache> context =
                builder.value(valueSerializer).build();
        return new ReactiveRedisTemplate<>(factory, context);
    }
}