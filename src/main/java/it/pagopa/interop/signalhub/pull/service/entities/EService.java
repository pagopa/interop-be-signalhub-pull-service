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
@Table("ORGANIZATION_ESERVICE")
public class EService implements Serializable {

    @Id
    @Column("eservice_id")
    private String eserviceId;

    @Column("organization_id")
    private String organizationId;

    @Column("state")
    private String state;

    @Column("tmst_insert")
    private Timestamp tmstInsert;

    @Column("tmst_last_edit")
    private Timestamp tmstLastEdit;

}
