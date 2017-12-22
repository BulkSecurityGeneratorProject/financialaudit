package com.bkavramlari.financialaudit.service.financial.parser.csv;

import com.bkavramlari.financialaudit.domain.enums.UploadFileTypes;
import com.bkavramlari.financialaudit.service.financial.parser.csv.exceptions.CsvParseServiceException;
import com.bkavramlari.financialaudit.service.financial.parser.csv.pojo.HesapHareketleriCsvPojo;
import com.bkavramlari.financialaudit.service.financial.parser.csv.pojo.HesapPlaniCsvPojo;
import com.bkavramlari.financialaudit.service.financial.parser.csv.pojo.KitapCsvPojo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.input.BOMInputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by yildizib on 03/11/2017.
 */
@Data
@Slf4j
@Component
public class CsvParseService {

    @Value("${upload.UPLOAD_DIRECTORY}")
    private String mainDirectory;
    @Value("${upload.CSV_FILE_DELIMITER}")
    private String csvDelimiter;

    private enum PojoTypes {
        KITAP,
        HESAP_PLANI,
        HESAP_HAREKETLERI
    }
    /**
     * @param filePath
     * @param fileName
     * @return
     */
    public <T> List<T> parse(String filePath, String fileName, PojoTypes type) throws CsvParseServiceException {
        String fileFullPath = mainDirectory + filePath + fileName;
        List<T> result = null;
        try {

            Map<String, String> fieldToPropMap = null;
            switch (type) {
                case KITAP:
                    fieldToPropMap = KitapCsvPojo.getFieldTOPropMap();
                    break;
                case HESAP_PLANI:
                    fieldToPropMap = HesapPlaniCsvPojo.getFieldTOPropMap();
                    break;
                case HESAP_HAREKETLERI:
                    fieldToPropMap = HesapHareketleriCsvPojo.getFieldTOPropMap();
                    break;
            }

            final Reader reader = new InputStreamReader(new BOMInputStream(new FileInputStream(fileFullPath)), "UTF-8");
            final CSVParser parser = new CSVParser(reader, CSVFormat.RFC4180
                    .withFirstRecordAsHeader()
                    .withDelimiter(csvDelimiter.charAt(0))
                    .withIgnoreSurroundingSpaces());
            result = new LinkedList<>();
            for (CSVRecord record : parser) {

                T row = null;
                switch (type) {
                    case KITAP:
                        row = (T) new KitapCsvPojo();
                        break;
                    case HESAP_PLANI:
                        row = (T) new HesapPlaniCsvPojo();
                        break;
                    case HESAP_HAREKETLERI:
                        row = (T) new HesapHareketleriCsvPojo();
                        break;
                }

                Iterator<String> it = fieldToPropMap.keySet().iterator();
                while (it.hasNext()) {
                    String propName = it.next();
                    String fieldName = fieldToPropMap.get(propName);

                    Field f = row.getClass().getDeclaredField(propName);
                    f.setAccessible(true);
                    f.set(row, record.get(fieldName));
                }

                result.add(row);
            }

        } catch (IOException e) {
            log.error(e.getMessage());
            throw new CsvParseServiceException(e.getMessage());
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * @param filePath
     * @param fileName
     * @param fileType
     * @param <T>
     * @return
     */
    public <T> List<T> parse(String filePath, String fileName, UploadFileTypes fileType) throws CsvParseServiceException {

        List<T> result = null;
        switch (fileType) {
            case HESAP_PLANI:
                result = parse(filePath, fileName, PojoTypes.HESAP_PLANI);
                break;
            case KITAP:
                result = parse(filePath, fileName, PojoTypes.KITAP);
                break;
            case HESAP_HAREKETLERI:
                result = parse(filePath, fileName, PojoTypes.HESAP_HAREKETLERI);
                break;
            case KDV:
                throw new CsvParseServiceException("Dosya tipi gecersiz. Bu XML bekleniyor.");
            case ALIM_FORMU:
                throw new CsvParseServiceException("Dosya tipi gecersiz. Bu XML bekleniyor.");
            case SATIS_FORMU:
                throw new CsvParseServiceException("Dosya tipi gecersiz. Bu XML bekleniyor.");
            default:
                throw new CsvParseServiceException("Dosya tipi gecersiz.");
        }

        return result;
    }
}
