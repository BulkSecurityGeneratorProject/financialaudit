package com.bkavramlari.financialaudit.service.financial.parser.xml.pojo;

import lombok.Data;
import lombok.ToString;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.List;

/**
 * ALIM FORMU (FORMBA)
 * <p>
 * Created by yildizib on 04/11/2017.
 */
@Data
@XmlRootElement(name = "beyanname")
@XmlAccessorType(XmlAccessType.FIELD)
public class AlimFormu implements Serializable {

    @XmlElement
    private AFGenel genel;
    @XmlElement
    private Ozel ozel;


    /**
     *
     */
    @Data
    @XmlRootElement(name = "genel")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class AFGenel implements Serializable {

        @XmlElement
        private Idari idari;
        @XmlElement
        private Mukellef mukellef;
        @XmlElement
        private Duzenleyen duzenleyen;
        @XmlElement
        private HSV hsv;
        @XmlElement
        private Gonderen gonderen;
        @XmlElement
        private YMM ymm;
    }

    /**
     *
     */
    @Data
    @XmlRootElement(name = "idari")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Idari implements Serializable {

        @XmlElement(name = "vdKodu")
        private String vdKodu;
        @XmlElement
        private Donem donem;
    }

    /**
     *
     */
    @Data
    @XmlRootElement(name = "donem")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Donem implements Serializable {

        @XmlElement(name = "ay")
        private String ay;
        @XmlElement(name = "yil")
        private String yil;
        @XmlElement(name = "tip")
        private String tip;
    }

    /**
     *
     */
    @Data
    @XmlRootElement(name = "mukellef")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Mukellef implements Serializable {
        @XmlElement(name = "vergiNo")
        private String vergiNo;
        @XmlElement(name = "soyadi")
        private String soyadi;
        @XmlElement(name = "adi")
        private String adi;
        @XmlElement(name = "eposta")
        private String eposta;
        @XmlElement(name = "alanKodu")
        private String alanKodu;
        @XmlElement(name = "telNo")
        private String telNo;
    }

    /**
     *
     */
    @Data
    @ToString(callSuper = true)
    @XmlRootElement(name = "duzenleyen")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Duzenleyen extends Mukellef {
    }

    /**
     *
     */
    @Data
    @ToString(callSuper = true)
    @XmlRootElement(name = "hsv")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class HSV extends Mukellef {
    }

    /**
     *
     */
    @Data
    @ToString(callSuper = true)
    @XmlRootElement(name = "gonderen")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Gonderen extends Mukellef {
    }

    /**
     *
     */
    @Data
    @ToString(callSuper = true)
    @XmlRootElement(name = "ymm")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class YMM extends Mukellef {
    }

    /**
     *
     */
    @Data
    @ToString
    @XmlRootElement(name = "ozel")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Ozel implements Serializable {
        @XmlElement(name = "toplamAlimBedeli")
        private String toplamAlimBedeli;
        @XmlElement
        private AlisBildirimi alisBildirimi;
    }

    /**
     *
     */
    @Data
    @ToString
    @XmlRootElement(name = "alisBildirimi")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class AlisBildirimi implements Serializable {
        @XmlElement
        private List<BA> ba;
    }

    /**
     *
     */
    @Data
    @ToString
    @XmlRootElement(name = "ba")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class BA implements Serializable {
        @XmlElement(name = "siraNo")
        private String siraNo;
        @XmlElement(name = "unvan")
        private String unvan;
        @XmlElement(name = "ulke")
        private String ulke;
        @XmlElement(name = "vkno")
        private String vkno;
        @XmlElement(name = "tckimlikno")
        private String tckimlikno;
        @XmlElement(name = "belgeSayisi")
        private String belgeSayisi;
        @XmlElement(name = "malHizmetBedeli")
        private String malHizmetBedeli;
    }
}