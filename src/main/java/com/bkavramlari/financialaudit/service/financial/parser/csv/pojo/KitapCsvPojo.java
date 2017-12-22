package com.bkavramlari.financialaudit.service.financial.parser.csv.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Kitap CSV dosyasinin POJO sema haritalamasidir.
 * POJO object mapper for Kitap csv
 * <p>
 * Created by yildizib on 03/11/2017.
 */
@Data
@ToString
@EqualsAndHashCode
public class KitapCsvPojo implements Serializable {

    @CsvBindByName(column = "muhasebekodu")
    private String muhasebeKodu;
    @CsvBindByName(column = "carikodu")
    private String cariKodu;
    @CsvBindByName(column = "vergino")
    private String vergiNo;
    @CsvBindByName(column = "unvan")
    private String unvan;
    @CsvBindByName(column = "vergidairesi")
    private String vergiDairesi;

    /**
     * @return
     */
    public static Map<String, String> getFieldTOPropMap() {
        Map<String, String> list = new LinkedHashMap<>();

        for (Field f : KitapCsvPojo.class.getDeclaredFields()) {
            CsvBindByName column = f.getAnnotation(CsvBindByName.class);
            if (column != null) {
                list.put(f.getName(), column.column());
            }
        }

        return list;
    }

}