package com.bkavramlari.financialaudit.domain.financial;

import com.bkavramlari.financialaudit.domain.base.ModelBase;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "BAGENEL")
public class BAGenel extends ModelBase implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_BAGNL_ID")
    @SequenceGenerator(name = "SEQ_BAGNL_ID", sequenceName = "SEQ_BAGNL_ID", initialValue = 1, allocationSize = 1)
    private Long id;
    @Column(name = "UPLOAD_ID", nullable = false)
    private Long uploadid;
    @Column(name = "FG_ID", length = 10)
    private String fgid;
    @Column(name = "VDKODU", length = 11)
    private String vdkodu;
    @Column(name = "DONEMTIP", length = 11, nullable = false)
    private String donemtip;
    @Column(name = "DONEMYIL", nullable = false)
    private Short donemyil;
    @Column(name = "DONEMAY", nullable = false)
    private Byte donemay;
    @Column(name = "MUKELLEF", length = 11)
    private String mukellef;
    @Column(name = "HSV", length = 255)
    private String hsv;
    @Column(name = "DUZENLEYEN", length = 255)
    private String duzenleyen;
    @Column(name = "GONDEREN", length = 255)
    private String gonderen;
    @Column(name = "YMM", length = 255)
    private String ymm;

}