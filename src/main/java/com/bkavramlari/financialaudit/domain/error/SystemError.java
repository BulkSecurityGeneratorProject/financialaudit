package com.bkavramlari.financialaudit.domain.error;

import com.bkavramlari.financialaudit.domain.base.AuditBase;
import com.bkavramlari.financialaudit.domain.enums.ErrorType;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "SYSTEM_ERRORS")
public class SystemError extends AuditBase implements Serializable {

    private static final long serialVersionUID = -687874117917352477L;

    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @Column(name = "ERROR_TYPE")
    @Enumerated(EnumType.STRING)
    private ErrorType errorType;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "ERROR_DESCRIPTION")
    private String errorDescription;
}