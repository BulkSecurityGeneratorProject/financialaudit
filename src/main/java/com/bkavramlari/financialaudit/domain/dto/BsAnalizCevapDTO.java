package com.bkavramlari.financialaudit.domain.dto;

import lombok.Data;

/**
 * Created by yildizib on 07/11/2017.
 */
@Data
public class BsAnalizCevapDTO extends ErrorDTO {

    private Double bsViewToplamAlacak;
    private Double bsViewToplamBorc;
    private Long bsViewToplamBelgeSayisi;
    private String bsViewVergiNo;
    private String bsViewUnvan;
    private Long bsOzelBelgeSayisi;
    private Double bsViewToplamAlacakveBorcFarki;
    private String bsViewMesaj;
    private Integer bsViewSatirNo;
    private Double bsOzelMalHizmetBedeli;
    private AnalizDurumlari bsViewSonuc;

    /**
     *
     */
    public enum AnalizDurumlari {
        BELGE_SAYILARI_ESIT,
        BELGE_SAYILARI_FARKLI,
        BELGE_SAYILARI_YOK
    }

    /**
     *
     */
    public BsAnalizCevapDTO() {

    }

}



