package com.bkavramlari.financialaudit.domain.financial;

import com.bkavramlari.financialaudit.domain.base.ModelBase;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "HESAPPLANI")
public class HesapPlani extends ModelBase implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_HSPPLN_ID")
    @SequenceGenerator(name = "SEQ_HSPPLN_ID", sequenceName = "SEQ_HSPPLN_ID", initialValue = 1, allocationSize = 1)
    private Long id;
    @Column(name = "UPLOAD_ID", nullable = false)
    private Long uploadid;
    @Column(name = "HESAPKODU")
    private String hesapkodu;
    @Column(name = "HESAPADI")
    private String hesapadi;
    @Column(name = "GRUP1")
    private String grup1;
    @Column(name = "GRUP2")
    private String grup2;
    @Column(name = "GRUP3")
    private String grup3;
    @Column(name = "GRUP4")
    private String grup4;
    @Column(name = "AKTIFPASIF", length = 5)
    private String aktifpasif;
    @Column(name = "BILANCOTABLOSU")
    private Long bilancotablosu;
    @Column(name = "GELIRTABLOSU")
    private Long gelirtablosu;
    @Column(name = "GIDERTABLOSU")
    private Long gidertablosu;
    @Column(name = "NAKITTABLOSU")
    private Long nakittablosu;
    @Column(name = "NAZIMHESAP")
    private Long nazimhesap;
    @Column(name = "OZELTABLOSU")
    private Long ozeltablosu;
    @Column(name = "BORCBAKIYE")
    private Long borcbakiye;
    @Column(name = "ALACAKBAKIYE")
    private Long alacakbakiye;
    @Column(name = "HESAPADIYABANCIDIL")
    private Long hesapadiyabancidil;
    @Column(name = "GRUP1YABANCIDIL")
    private Long grup1yabancidil;
    @Column(name = "GRUP2YABANCIDIL")
    private Long grup2yabancidil;
    @Column(name = "GRUP3YABANCIDIL")
    private Long grup3yabancidil;
    @Column(name = "GRUP4YABANCIDIL")
    private Long grup4yabancidil;
    @Column(name = "OZEL1")
    private Long ozel1;
    @Column(name = "OZEL2")
    private Long ozel2;
    @Column(name = "DOVIZCINSI", length = 11)
    private String dovizcinsi;
    @Column(name = "SENEKAPATMAHESABI")
    private Long senekapatmahesabi;
    @Column(name = "SENEKAPATMASIRASI")
    private Long senekapatmasirasi;

}