package com.bkavramlari.financialaudit.domain.dto;

import lombok.Data;

/**
 * Created by yildizib on 07/11/2017.
 */
@Data
public class BaAnalizCevapDTO extends ErrorDTO {

    private Double baViewToplamAlacak;
    private Double baViewToplamBorc;
    private Long baViewToplamBelgeSayisi;
    private String baViewVergiNo;
    private String baViewUnvan;
    private Long baOzelBelgeSayisi;
    private Double baViewToplamAlacakveBorcFarki;
    private String baViewMesaj;
    private Integer baViewSatirNo;
    private Double baOzelMalHizmetBedeli;
    private AnalizDurumlari baViewSonuc;

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
    public BaAnalizCevapDTO() {

    }

}



