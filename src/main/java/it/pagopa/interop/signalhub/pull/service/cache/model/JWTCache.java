package it.pagopa.interop.signalhub.pull.service.cache.model;


import lombok.*;
import org.springframework.data.redis.core.RedisHash;


@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("jwt")
public class JWTCache {
    private String jwt;
}
