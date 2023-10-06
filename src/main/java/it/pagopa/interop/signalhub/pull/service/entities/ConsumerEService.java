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
    public static final String COLUMN_STATE = "state";
    public static final String COLUMN_DATE_INSERT = "tmst_insert";
    public static final String COLUMN_DATE_UPDATE = "tmst_last_edit";

    @Id
    @Column("eservice_id")
    private String eserviceId;

    @Column("consumer_id")
    private String consumerId;

    @Column("state")
    private String state;

    @Column("tmst_insert")
    private Timestamp tmstInsert;

    @Column("tmst_last_edit")
    private Timestamp tmstLastEdit;

}
