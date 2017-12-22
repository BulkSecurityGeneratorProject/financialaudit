package com.bkavramlari.financialaudit.domain.financial;

import com.bkavramlari.financialaudit.domain.base.ModelBase;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "BAOZEL")
public class BAOzel extends ModelBase implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_BAOZL_ID")
    @SequenceGenerator(name = "SEQ_BAOZL_ID", sequenceName = "SEQ_BAOZL_ID", initialValue = 1, allocationSize = 1)
    private Long id;
    @Column(name = "FO_ID", length = 255)
    private String foid;
    @Column(name = "BA_GENEL_ID", nullable = false)
    private Long bagenelid;
    @Column(name = "SIRANO", nullable = false)
    private Long sirano;
    @Column(name = "BELGESAYISI", nullable = false)
    private Long belgesayisi;
    @Column(name = "MALHIZMETBEDELI", nullable = false)
    private Double malhizmetbedeli;
    @Column(name = "TOPLAMALIMBEDELI")
    private Double toplamalimbedeli;
    @Column(name = "UPLOAD_ID", nullable = false)
    private Long uploadid;
    @Column(name = "UNVAN", length = 255, nullable = false)
    private String unvan;
    @Column(name = "ULKE", length = 255, nullable = false)
    private String ulke;
    @Column(name = "VKNO", length = 255, nullable = false)
    private String vkNo;
    @Column(name = "BEYAN")
    private Long beyan;

}