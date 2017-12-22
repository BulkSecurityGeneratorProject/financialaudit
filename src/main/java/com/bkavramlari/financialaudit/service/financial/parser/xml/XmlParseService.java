package com.bkavramlari.financialaudit.service.financial.parser.xml;

import com.bkavramlari.financialaudit.domain.enums.UploadFileTypes;
import com.bkavramlari.financialaudit.service.financial.parser.xml.exceptions.XmlParseServiceException;
import com.bkavramlari.financialaudit.service.financial.parser.xml.pojo.AlimFormu;
import com.bkavramlari.financialaudit.service.financial.parser.xml.pojo.KdvFormu;
import com.bkavramlari.financialaudit.service.financial.parser.xml.pojo.SatisFormu;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

/**
 * Created by yildizib on 03/11/2017.
 */
@Data
@Slf4j
@Component
public class XmlParseService {

    @Value("${upload.UPLOAD_DIRECTORY}")
    private String mainDirectory;

    /**
     * @param filePath
     * @param fileName
     * @return
     */
    public <T> T parse(String filePath, String fileName, Class<T> type) throws XmlParseServiceException {
        String fileFullPath = mainDirectory + filePath + fileName;
        T result = null;
        try {

            File file = new File(fileFullPath);
            JAXBContext jaxbContext = JAXBContext.newInstance(type);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            result = (T) jaxbUnmarshaller.unmarshal(file);
            System.out.println(result);

        } catch (JAXBException e) {
            log.error(e.getMessage());
            throw new XmlParseServiceException(e.getMessage());
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
    public <T> T parse(String filePath, String fileName, UploadFileTypes fileType) throws XmlParseServiceException {

        T result = null;
        switch (fileType) {
            case HESAP_PLANI:
                throw new XmlParseServiceException("Dosya tipi gecersiz. Parser Seciniz.");
            case KITAP:
                throw new XmlParseServiceException("Dosya tipi gecersiz. Parser Seciniz.");
            case HESAP_HAREKETLERI:
                throw new XmlParseServiceException("Dosya tipi gecersiz. Parser Seciniz.");
            case KDV:
                result = parse(filePath, fileName, (Class<T>) KdvFormu.class);
                break;
            case ALIM_FORMU:
                result = parse(filePath, fileName, (Class<T>) AlimFormu.class);
                break;
            case SATIS_FORMU:
                result = parse(filePath, fileName, (Class<T>) SatisFormu.class);
                break;
            default:
                throw new XmlParseServiceException("Dosya tipi gecersiz.");
        }

        return result;
    }
}
