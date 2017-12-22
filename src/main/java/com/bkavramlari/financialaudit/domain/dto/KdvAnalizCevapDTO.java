package com.bkavramlari.financialaudit.domain.dto;

import lombok.Data;

import java.util.List;

/**
 * Created by yildizib on 07/11/2017.
 */
@Data
public class KdvAnalizCevapDTO extends ErrorDTO {

    private List<String> hesapKodlari;
    private List<String> kebirKodlari;
    private Long kdvOrani;
    private String firmaAdi;
    private String vergiNo;
    private Double kdvTutari;
    private Double borcTutari;
    private AnalizDurumlari sonuc;
    private String mesaj;
    private AnalizToplamTipi tip;

    public KdvAnalizCevapDTO() {

    }

    /**
     *
     */
    public enum AnalizDurumlari {
        TUTARLAR_ESIT,
        TUTARLAR_FARKLI
    }

    /**
     *
     */
    public enum AnalizToplamTipi {
        HESAP_KODU_BORC,
        HESAP_KODU_ALACAK,
        KEBIR_KODU_BORC,
        KEBIR_KODU_ALACAK
    }

}



