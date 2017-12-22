package com.bkavramlari.financialaudit.domain.financial;

import com.bkavramlari.financialaudit.domain.base.ModelBase;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "TEVKIFATTABLOSU")
public class TevkifatTablosu extends ModelBase implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TVKFTTBL_ID")
    @SequenceGenerator(name = "SEQ_TVKFTTBL_ID", sequenceName = "SEQ_TVKFTTBL_ID", initialValue = 1, allocationSize = 1)
    private Long id;
    @Column(name = "UPLOAD_ID", nullable = false)
    private Long uploadid;
    @Column(name = "KDVOZEL_ID", nullable = false)
    private Long kdvozelid;
    @Column(name = "BA_GENEL_ID", nullable = false)
    private Long bagenelid;
    @Column(name = "MATRAH", nullable = false)
    private Double matrah;
    @Column(name = "ORAN", nullable = false)
    private Long oran;
    @Column(name = "VERGI", length = 100, nullable = false)
    private Double vergi;
    @Column(name = "UYGULANDIMI")
    private Byte uygulandimi;

}