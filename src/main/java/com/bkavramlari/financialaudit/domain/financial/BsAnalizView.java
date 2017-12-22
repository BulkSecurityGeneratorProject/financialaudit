package com.bkavramlari.financialaudit.domain.financial;

import lombok.Data;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * ilk olarak view create edilirse sorun yok. Yoksa entity table olarak create olur.
 */
@Data
@Entity
@Immutable
@Table(name = "BS_ANALIZ_VIEW")
public class BsAnalizView implements Serializable {

    @Id
    @Column(name = "ID")
    private Long id;
    @Column(name = "ALACAK")
    private Double alacak;
    @Column(name = "CALACAK")
    private Long cAlacak;
    @Column(name = "BORC")
    private Double borc;
    @Column(name = "VERGINO")
    private String vergiNo;
    @Column(name = "HH_UPLOAD_ID")
    private Long hhUploadId;
    @Column(name = "K_UPLOAD_ID")
    private Long kUploadId;
    @Column(name = "B_UPLOAD_ID")
    private Long bUploadId;

    public BsAnalizView() {

    }

    public BsAnalizView(Double alacak, Long cAlacak, Double borc, String vergiNo) {
        this.alacak = alacak;
        this.cAlacak = cAlacak;
        this.borc = borc;
        this.vergiNo = vergiNo;
    }
}
