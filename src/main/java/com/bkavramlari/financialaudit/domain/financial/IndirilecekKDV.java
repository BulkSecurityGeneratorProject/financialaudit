package com.bkavramlari.financialaudit.domain.financial;

import com.bkavramlari.financialaudit.domain.base.ModelBase;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "INDIRILECEKKDV")
public class IndirilecekKDV extends ModelBase implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_INDKDV_ID")
    @SequenceGenerator(name = "SEQ_INDKDV_ID", sequenceName = "SEQ_INDKDV_ID", initialValue = 1, allocationSize = 1)
    private Long id;
    @Column(name = "ORAN", nullable = false)
    private Long oran;
    @Column(name = "BEDEL", length = 100, nullable = false)
    private Double bedel;
    @Column(name = "KDVTUTARI", length = 100, nullable = false)
    private Double kdvtutari;
    @Column(name = "KDVOZEL_ID", nullable = false)
    private Long kdvozelid;
    @Column(name = "UPLOAD_ID", nullable = false)
    private Long uploadid;
    @Column(name = "BA_GENEL_ID", nullable = false)
    private Long bagenelid;

}