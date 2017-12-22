package com.bkavramlari.financialaudit.domain.financial;

import com.bkavramlari.financialaudit.domain.base.ModelBase;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "INDIRIMLER")
public class Indirimler extends ModelBase implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_INDRMLR_ID")
    @SequenceGenerator(name = "SEQ_INDRMLR_ID", sequenceName = "SEQ_INDRMLR_ID", initialValue = 1, allocationSize = 1)
    private Long id;
    @Column(name = "KDVOZEL_ID", nullable = false)
    private Long kdvozelid;
    @Column(name = "INDIRIMTURU", length = 100, nullable = false)
    private String indirimturu;
    @Column(name = "VERGI", length = 100, nullable = false)
    private Double vergi;

}
