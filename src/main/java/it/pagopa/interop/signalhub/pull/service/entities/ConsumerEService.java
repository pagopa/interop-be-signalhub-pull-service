package it.pagopa.interop.signalhub.pull.service.entities;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
@ToString
@Table("consumer_eservice")
public class ConsumerEService implements Serializable {
    public static final String COLUMN_ESERVICE_ID = "eservice_id";
    public static final String COLUMN_CONSUMER_ID = "consumer_id";
    public static final String COLUMN_DESCRIPTOR_ID = "descriptor_id";
    public static final String COLUMN_EVENT_ID = "event_id";
    public static final String COLUMN_AGREEMENT_ID = "agreement_id";
    public static final String COLUMN_STATE = "state";
    public static final String COLUMN_DATE_INSERT = "tmst_insert";
    public static final String COLUMN_DATE_UPDATE = "tmst_last_edit";

    @Id
    @Column(COLUMN_ESERVICE_ID)
    private String eserviceId;

    @Column(COLUMN_CONSUMER_ID)
    private String consumerId;

    @Column(COLUMN_DESCRIPTOR_ID)
    private String descriptorId;

    @Column(COLUMN_EVENT_ID)
    private Long eventId;

    @Column(COLUMN_AGREEMENT_ID)
    private String agreementId;

    @Column(COLUMN_STATE)
    private String state;

    @Column(COLUMN_DATE_INSERT)
    private Timestamp tmstInsert;

    @Column(COLUMN_DATE_UPDATE)
    private Timestamp tmstLastEdit;

}
