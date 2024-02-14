package it.pagopa.interop.signalhub.pull.service.entities;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.sql.Timestamp;
import java.util.Objects;

@Getter
@Setter
@ToString
@Table("ESERVICE")
public class EService {
    public static final String COLUMN_ESERVICE_ID = "eservice_id";
    public static final String COLUMN_PRODUCER_ID = "producer_id";
    public static final String COLUMN_DESCRIPTOR_ID = "descriptor_id";
    public static final String COLUMN_STATE = "state";
    public static final String COLUMN_EVENT_ID = "event_id";
    public static final String COLUMN_DATE_INSERT = "tmst_insert";
    public static final String COLUMN_DATE_UPDATE = "tmst_last_edit";

    @Id
    @Column(COLUMN_ESERVICE_ID)
    private String eserviceId;

    @Column(COLUMN_PRODUCER_ID)
    private String producerId;

    @Column(COLUMN_DESCRIPTOR_ID)
    private String descriptorId;

    @Column(COLUMN_STATE)
    private String state;

    @Column(COLUMN_EVENT_ID)
    private Long eventId;

    @Column(COLUMN_DATE_INSERT)
    private Timestamp tmstInsert;

    @Column(COLUMN_DATE_UPDATE)
    private Timestamp tmstLastEdit;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EService eService = (EService) o;
        return Objects.equals(eserviceId, eService.eserviceId) && Objects.equals(producerId, eService.producerId) && Objects.equals(descriptorId, eService.descriptorId) && Objects.equals(state, eService.state) && Objects.equals(eventId, eService.eventId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eserviceId, producerId, descriptorId, state, eventId);
    }
}
