package it.pagopa.interop.signalhub.pull.service.cache.model;


import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import java.sql.Timestamp;
import java.util.Objects;


@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("eservices")
public class EServiceCache {
    private String eserviceId;
    private String producerId;
    private String descriptorId;
    private String state;
    private Long eventId;
    private Timestamp tmstInsert;
    private Timestamp tmstLastEdit;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EServiceCache that = (EServiceCache) o;
        return Objects.equals(eserviceId, that.eserviceId) && Objects.equals(producerId, that.producerId) && Objects.equals(descriptorId, that.descriptorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eserviceId, producerId, descriptorId);
    }
}
