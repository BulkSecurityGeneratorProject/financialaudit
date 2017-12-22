package com.bkavramlari.financialaudit.service.financial.parser.csv.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Hesap plani CSV dosyasinin POJO sema haritalamasidir.
 * POJO object mapper for hesap plani csv
 * <p>
 * Created by yildizib on 03/11/2017.
 */
@Data
@ToString
@EqualsAndHashCode
public class HesapPlaniCsvPojo implements Serializable {

    @CsvBindByName(column = "Hesap Kodu")
    private String hesapKodu;
    @CsvBindByName(column = "Hesap Adı")
    private String hesapAdi;
    @CsvBindByName(column = "Grup 1")
    private String grup1;
    @CsvBindByName(column = "Grup 2")
    private String grup2;
    @CsvBindByName(column = "Grup 3")
    private String grup3;
    @CsvBindByName(column = "Grup 4")
    private String grup4;
    @CsvBindByName(column = "Aktif / Pasif")
    private String aktifPasif;
    @CsvBindByName(column = "Bilanço Tablosu")
    private String bilancoTablosu;
    @CsvBindByName(column = "Gelir Tablosu")
    private String gelirTablosu;
    @CsvBindByName(column = "Gider Tablosu")
    private String giderTablosu;
    @CsvBindByName(column = "Nakit Tablosu")
    private String nakitTablosu;
    @CsvBindByName(column = "Nazım Hesap")
    private String nazimHesap;
    @CsvBindByName(column = "Özel Tablosu")
    private String ozelTablosu;
    @CsvBindByName(column = "Borç Bakiye")
    private String borcBakiye;
    @CsvBindByName(column = "Alacak Bakiye")
    private String alacakBakiye;
    @CsvBindByName(column = "Hesap Adı (Yabancı Dil)")
    private String hesapAdiYabanciDil;
    @CsvBindByName(column = "Grup 1 (Yabancı Dil)")
    private String grup1YabanciDil;
    @CsvBindByName(column = "Grup 2 (Yabancı Dil)")
    private String grup2YabanciDil;
    @CsvBindByName(column = "Grup 3 (Yabancı Dil)")
    private String grup3YabanciDil;
    @CsvBindByName(column = "Grup 4 (Yabancı Dil)")
    private String grup4YabanciDil;
    @CsvBindByName(column = "Özel 1")
    private String ozel1;
    @CsvBindByName(column = "Özel 2")
    private String ozel2;
    @CsvBindByName(column = "Döviz Cinsi")
    private String dovizCinsi;
    @CsvBindByName(column = "Sene Kapatma Heabı")
    private String seneKapamaHesabi;
    @CsvBindByName(column = "Sene Kapatma Sırası")
    private String seneKapamaSirasi;

    /**
     * @return
     */
    public static Map<String, String> getFieldTOPropMap() {
        Map<String, String> list = new LinkedHashMap<>();

        for (Field f : HesapPlaniCsvPojo.class.getDeclaredFields()) {
            CsvBindByName column = f.getAnnotation(CsvBindByName.class);
            if (column != null) {
                list.put(f.getName(), column.column());
            }
        }

        return list;
    }
}