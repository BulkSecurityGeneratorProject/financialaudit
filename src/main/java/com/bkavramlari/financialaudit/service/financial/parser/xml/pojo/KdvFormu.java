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
 * KDV FORMU
 * Created by yildizib on 04/11/2017.
 */
@Data
@XmlRootElement(name = "beyanname")
@XmlAccessorType(XmlAccessType.FIELD)
public class KdvFormu implements Serializable {
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
    @ToString
    @XmlRootElement(name = "ozel")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Ozel implements Serializable {
        @XmlElement
        private TevkifatUygulanmayanlar tevkifatUygulanmayanlar;
        @XmlElement(name = "vergiToplami")
        private String vergiToplami;
        @XmlElement(name = "toplamMatrah")
        private String toplamMatrah;
        @XmlElement(name = "hesaplananKDV")
        private String hesaplananKDV;
        @XmlElement(name = "ilaveEdilecekKDV")
        private String ilaveEdilecekKDV;
        @XmlElement(name = "toplamKDV")
        private String toplamKDV;
        @XmlElement
        private Indirimler indirimler;
        @XmlElement(name = "indirimlerToplami")
        private String indirimlerToplami;
        @XmlElement
        private IndirilecekKDVODler indirilecekKDVODler;
        @XmlElement(name = "indirilecekKDVODToplamKDV")
        private String indirilecekKDVODToplamKDV;
        @XmlElement(name = "ihracGerceklesmeyenTecilEdilemeyenKDV")
        private String ihracGerceklesmeyenTecilEdilemeyenKDV;
        @XmlElement(name = "kdvsizTeminEdilenlerIcinOdenmeyenKDV")
        private String kdvsizTeminEdilenlerIcinOdenmeyenKDV;
        @XmlElement(name = "ihracatDonemindeIadeEdilecekKDV")
        private String ihracatDonemindeIadeEdilecekKDV;
        @XmlElement(name = "yuklenilenKDV")
        private String yuklenilenKDV;
        @XmlElement(name = "ihracVergiFark")
        private String ihracVergiFark;
        @XmlElement(name = "tecilEdilecekKDV")
        private String tecilEdilecekKDV;
        @XmlElement(name = "odenmesiGerekenKDV")
        private String odenmesiGerekenKDV;
        @XmlElement(name = "iadeEdilmesiGerekenKDV")
        private String iadeEdilmesiGerekenKDV;
        @XmlElement(name = "sonrakiDonemeDevredenKDV")
        private String sonrakiDonemeDevredenKDV;
        @XmlElement(name = "matrahaDahilOlmayanBedel")
        private String matrahaDahilOlmayanBedel;
        @XmlElement(name = "teslimVeHizmetleriTeskilEdenBedelAylik")
        private String teslimVeHizmetleriTeskilEdenBedelAylik;
        @XmlElement(name = "teslimVeHizmetleriTeskilEdenBedelKumulatif")
        private String teslimVeHizmetleriTeskilEdenBedelKumulatif;
        @XmlElement(name = "krediKarti")
        private String krediKarti;
    }

    /**
     *
     */
    @Data
    @ToString
    @XmlRootElement(name = "tevkifatUygulanmayanlar")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class TevkifatUygulanmayanlar implements Serializable {
        @XmlElement
        private List<TevkifatUygulanmayan> tevkifatUygulanmayan;
    }

    /**
     *
     */
    @Data
    @ToString
    @XmlRootElement(name = "tevkifatUygulanmayan")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class TevkifatUygulanmayan implements Serializable {
        @XmlElement(name = "matrah")
        private String matrah;
        @XmlElement(name = "oran")
        private String oran;
        @XmlElement(name = "vergi")
        private String vergi;
    }

    /**
     *
     */
    @Data
    @ToString
    @XmlRootElement(name = "indirimler")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Indirimler implements Serializable {
        @XmlElement
        private List<Indirim> indirim;
    }

    /**
     *
     */
    @Data
    @ToString
    @XmlRootElement(name = "indirim")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class Indirim implements Serializable {
        @XmlElement(name = "indirimTuru")
        private String indirimTuru;
        @XmlElement(name = "vergi")
        private String vergi;
    }

    /**
     *
     */
    @Data
    @ToString
    @XmlRootElement(name = "indirilecekKDVODler")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class IndirilecekKDVODler implements Serializable {
        @XmlElement
        private List<IndirilecekKDVOD> indirilecekKDVOD;
    }

    /**
     *
     */
    @Data
    @ToString
    @XmlRootElement(name = "indirilecekKDVOD")
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class IndirilecekKDVOD implements Serializable {
        @XmlElement(name = "oran")
        private String oran;
        @XmlElement(name = "bedel")
        private String bedel;
        @XmlElement(name = "KDVTutari")
        private String KDVTutari;
    }
}