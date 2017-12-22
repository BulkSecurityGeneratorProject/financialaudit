package com.bkavramlari.financialaudit.service.financial.parser.csv.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Hesap hareketleri CSV dosyasinin POJO sema haritalamasidir.
 * POJO object mapper for Hesap hareketleri csv
 * <p>
 * Created by yildizib on 03/11/2017.
 */
@Data
@ToString
@EqualsAndHashCode
public class HesapHareketleriCsvPojo implements Serializable {

    @CsvBindByName(column = "ay")
    private String ay;
    @CsvBindByName(column = "fistarihi")
    private String fisTarihi;
    @CsvBindByName(column = "yevmiyeno")
    private String yevmiyeNo;
    @CsvBindByName(column = "kebirkodu")
    private String kebirKodu;
    @CsvBindByName(column = "kebiradi")
    private String kebirAdi;
    @CsvBindByName(column = "muhasebehesapkodu")
    private String muhasebeHesapKodu;
    @CsvBindByName(column = "muhasebehesapadi")
    private String muhasebeHesapAdi;
    @CsvBindByName(column = "borc")
    private String borc;
    @CsvBindByName(column = "alacak")
    private String alacak;
    @CsvBindByName(column = "aciklama")
    private String aciklama;

    /**
     * @return
     */
    public static Map<String, String> getFieldTOPropMap() {
        Map<String, String> list = new LinkedHashMap<>();

        for (Field f : HesapHareketleriCsvPojo.class.getDeclaredFields()) {
            CsvBindByName column = f.getAnnotation(CsvBindByName.class);
            if (column != null) {
                list.put(f.getName(), column.column());
            }
        }

        return list;
    }
}