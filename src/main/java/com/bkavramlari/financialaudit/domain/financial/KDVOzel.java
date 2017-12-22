package com.bkavramlari.financialaudit.domain.financial;

import com.bkavramlari.financialaudit.domain.base.ModelBase;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "KDVOZEL")
public class KDVOzel extends ModelBase implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_KDVOZL_ID")
    @SequenceGenerator(name = "SEQ_KDVOZL_ID", sequenceName = "SEQ_KDVOZL_ID", initialValue = 1, allocationSize = 1)
    private Long id;
    @Column(name = "UPLOAD_ID", nullable = false)
    private Long uploadid;
    @Column(name = "VERGITOPLAMI", nullable = false)
    private Double vergitoplami;
    @Column(name = "TOPLAMMATRAH", nullable = false)
    private Double toplammatrah;
    @Column(name = "HESAPLANANKDV", nullable = false)
    private Double hesaplanankdv;
    @Column(name = "ILAVEEDILECEKKDV", nullable = false)
    private Double ilaveedilecekkdv;
    @Column(name = "TOPLAMKDV", nullable = false)
    private Double toplamkdv;
    @Column(name = "INDIRIMLERTOPLAMI", nullable = false)
    private Double indirimlertoplami;
    @Column(name = "INDIRILECEKKDVODTOPLAMKDV", nullable = false)
    private Double indirilecekkdvodtoplamkdv;
    @Column(name = "IHRACGERCEKLESMEYENTECILEDILEMEYENKDV", nullable = false)
    private Double ihracgerceklesmeyenteciledilemeyenkdv;
    @Column(name = "KDVSIZTEMINEDILENLERICINODENMEYENKDV", nullable = false)
    private Double kdvsizteminedilenlericinodenmeyenkdv;
    @Column(name = "IHRACATDONEMINDEIADEEDILECEKKDV", nullable = false)
    private Double ihracatdonemindeiadeedilecekkdv;
    @Column(name = "YUKLENILENKDV", nullable = false)
    private Double yuklenilenkdv;
    @Column(name = "IHRACVERGIFARK", nullable = false)
    private Double ihracvergifark;
    @Column(name = "TECILEDILECEKKDV", nullable = false)
    private Double teciledilecekkdv;
    @Column(name = "ODENMESIGEREKENKDV", nullable = false)
    private Double odenmesigerekenkdv;
    @Column(name = "IADEEDILMESIGEREKENKDV", nullable = false)
    private Double iadeedilmesigerekenkdv;
    @Column(name = "SONRAKIDONEMEDEVREDENKDV", nullable = false)
    private Double sonrakidonemedevredenkdv;
    @Column(name = "MATRAHADAHILOLMAYANBEDEL", nullable = false)
    private Double matrahadahilolmayanbedel;
    @Column(name = "TESLIMVEHIZMETLERITESKILEDENBEDELAYLIK", nullable = false)
    private Double teslimvehizmetleriteskiledenbedelaylik;
    @Column(name = "TESLIMVEHIZMETLERITESKILEDENBEDELKUMULATIF", nullable = false)
    private Double teslimvehizmetleriteskiledenbedelkumulatif;
    @Column(name = "KREDIKARTI", nullable = false)
    private Double kredikarti;

}