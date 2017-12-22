package com.bkavramlari.financialaudit.domain.oauth;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "PERSISTENT_AUDIT_EVENT", indexes = {
        @Index(name = "IDX_PERSISTENT_AUDIT_EVENT", columnList = "PRINCIPAL,EVENT_DATE")
})
public class PersistentAuditEvent implements Serializable {

    @Id
    @Column(name = "EVENT_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PRSTAUDITEVNT_ID")
    @SequenceGenerator(name = "SEQ_PRSTAUDITEVNT_ID", sequenceName = "SEQ_PRSTAUDITEVNT_ID", initialValue = 1, allocationSize = 1)
    private Long eventId;
    @Column(name = "PRINCIPAL", length = 50, nullable = false)
    private String principal;
    @Column(name = "EVENT_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date eventDate;
    @Column(name = "EVENT_TYPE", length = 255)
    private String eventType;

}
