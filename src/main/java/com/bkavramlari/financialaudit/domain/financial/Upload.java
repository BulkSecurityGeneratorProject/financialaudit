package com.bkavramlari.financialaudit.domain.financial;

import com.bkavramlari.financialaudit.domain.base.AuditBase;
import com.bkavramlari.financialaudit.domain.enums.UploadFileProcStats;
import com.bkavramlari.financialaudit.domain.enums.UploadFileTypes;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Data
@Entity
@Table(name = "UPLOAD")
public class Upload extends AuditBase implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_UPLOAD_ID")
    @SequenceGenerator(name = "SEQ_UPLOAD_ID", sequenceName = "SEQ_UPLOAD_ID", initialValue = 1, allocationSize = 1)
    private Long id;

    @Column(name = "DOSYA_ADI", length = 255, nullable = false)
    private String dosyaadi;
    @Column(name = "DOSYA_YOLU", length = 255, nullable = false)
    private String dosyayolu;
    @Column(name = "DOSYA_UZANTISI", length = 15, nullable = false)

    private String dosyauzantisi;
    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "ORIGINAL_FILE_NAME", length = 255, nullable = false)
    private String originalFileName;
    @Column(name = "FILE_TYPE")
    @Enumerated(EnumType.STRING)
    private UploadFileTypes fileType;
    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    private UploadFileProcStats status;
    @Column(name = "PROCESS_START_DATE")
    private Date startDate;
    @Column(name = "PROCESS_END_DATE")
    private Date endDate;

}
