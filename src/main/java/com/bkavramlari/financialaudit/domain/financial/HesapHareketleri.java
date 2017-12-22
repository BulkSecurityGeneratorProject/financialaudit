package com.bkavramlari.financialaudit.domain.financial;

import com.bkavramlari.financialaudit.domain.base.ModelBase;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "HESAPHAREKETLERI")
public class HesapHareketleri extends ModelBase implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_HSPHRKLR_ID")
    @SequenceGenerator(name = "SEQ_HSPHRKLR_ID", sequenceName = "SEQ_HSPHRKLR_ID", initialValue = 1, allocationSize = 1)
    private Long id;
    @Column(name = "UPLOAD_ID", nullable = false)
    private Long uploadid;
    @Column(name = "AY", nullable = false)
    private String ay;
    @Column(name = "FISTARIHI", nullable = false)
    private String fistarihi;
    @Column(name = "YEVMIYENO", nullable = false)
    private Long yevmiyeno;
    @Column(name = "KEBIRKODU", nullable = false)
    private String kebirkodu;
    @Column(name = "KEBIRADI", nullable = false)
    private String kebiradi;
    @Column(name = "MUHASEBEHESAPKODU", nullable = false)
    private String muhasebehesapkodu;
    @Column(name = "MUHASEBEHESAPADI", nullable = false)
    private String muhasebehesapadi;
    @Column(name = "BORC")
    private Double borc;
    @Column(name = "ALACAK")
    private Double alacak;
    @Column(name = "ACIKLAMA", length = 4000)
    private String aciklama;

}