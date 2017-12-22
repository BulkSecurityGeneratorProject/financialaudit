package com.bkavramlari.financialaudit.domain.oauth;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;


@Data
@Entity
@Table(name = "PERSISTENT_AUDIT_EVT_DATA", indexes = {
        @Index(name = "idx_persistent_audit_evt_data", columnList = "EVENT_ID")
})
public class PersistentAuditEvtData implements Serializable {

    @Id
    @Column(name = "EVENT_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PRSTAUDITEVNTDT_ID")
    @SequenceGenerator(name = "SEQ_PRSTAUDITEVNTDT_ID", sequenceName = "SEQ_PRSTAUDITEVNTDT_ID", initialValue = 1, allocationSize = 1)
    private Long eventId;
    @Column(name = "NAME", length = 150)
    private String name;
    @Column(name = "VALUE", length = 255)
    private String value;

}
