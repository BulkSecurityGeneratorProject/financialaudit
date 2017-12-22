package com.bkavramlari.financialaudit.domain.financial;

import com.bkavramlari.financialaudit.domain.base.ModelBase;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "FIRMAGENEL")
public class FirmaGenel extends ModelBase implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_FRMGNL_ID")
    @SequenceGenerator(name = "SEQ_FRMGNL_ID", sequenceName = "SEQ_FRMGNL_ID", initialValue = 1, allocationSize = 1)
    private Long id;
    @Column(name = "VERGINO", nullable = false)
    private String vergino;
    @Column(name = "SOYADI", nullable = false)
    private String soyadi;
    @Column(name = "ADI", nullable = false)
    private String adi;
    @Column(name = "EPOSTA", nullable = false)
    private String eposta;
    @Column(name = "ALANKODU", nullable = false)
    private Long alankodu;
    @Column(name = "TELNO", nullable = false)
    private Long telno;

}