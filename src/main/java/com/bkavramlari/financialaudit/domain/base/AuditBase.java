package com.bkavramlari.financialaudit.domain.base;


import com.bkavramlari.financialaudit.domain.enums.DataStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@MappedSuperclass
@Where(clause = "DATA_STATUS <> 'DELETED'")
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditBase extends ModelBase {

    @Basic(fetch = FetchType.LAZY)
    @JsonIgnore
    @CreatedDate
    @Column(name = "CREATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    public Date createdDate = new Date();

    @Basic(fetch = FetchType.LAZY)
    @JsonIgnore
    @LastModifiedDate
    @Column(name = "UPDATE_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate = new Date();

    @Basic(fetch = FetchType.LAZY)
    @NotNull
    @JsonIgnore
    @CreatedBy
    @Column(name = "CREATE_USER")
    private String createdBy;

    @Basic(fetch = FetchType.LAZY)
    @JsonIgnore
    @LastModifiedBy
    @Column(name = "UPDATE_USER")
    private String lastModifiedBy;

    @Basic(fetch = FetchType.LAZY)
    @Column(name = "DATA_STATUS")
    @Enumerated(EnumType.STRING)
    private DataStatus dataStatus = DataStatus.ACTIVE;

    @Basic(fetch = FetchType.LAZY)
    @NotNull
    @JsonIgnore
    @Version
    @Column(name = "VERSION")
    private Long version;
}