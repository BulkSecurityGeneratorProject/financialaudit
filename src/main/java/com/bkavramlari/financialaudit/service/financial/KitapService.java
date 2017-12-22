package com.bkavramlari.financialaudit.service.financial;

import com.bkavramlari.financialaudit.domain.enums.UploadFileTypes;
import com.bkavramlari.financialaudit.domain.financial.Kitap;
import com.bkavramlari.financialaudit.repository.financial.HesapHareketleriRepository;
import com.bkavramlari.financialaudit.repository.financial.KitapRepository;
import com.bkavramlari.financialaudit.service.financial.parser.csv.CsvParseService;
import com.bkavramlari.financialaudit.service.financial.parser.csv.exceptions.CsvParseServiceException;
import com.bkavramlari.financialaudit.service.financial.parser.csv.pojo.KitapCsvPojo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by yildizib on 05/11/2017.
 */
@Data
@Slf4j
@Service
public class KitapService {
    @Autowired
    private KitapRepository kitapRepository;
    @Autowired
    private HesapHareketleriRepository hesapHareketleriRepository;
    @Autowired
    private CsvParseService csvParseService;

    /**
     * @param pojo
     * @param uploadId
     * @return
     */
    @Transactional
    public Kitap save(KitapCsvPojo pojo, Long uploadId) {
        Kitap result = null;
        if (pojo == null) {
            throw new NullPointerException("KitapCsvPojo NULL olamaz!");
        }

        try {

            /* KITAP */
            result = new Kitap();
            result.setCarikodu(pojo.getCariKodu());
            result.setMuhasebekodu(pojo.getMuhasebeKodu());
            result.setUnvan(pojo.getUnvan());
            result.setUploadid(uploadId);
            result.setVergidairesi(pojo.getVergiDairesi());
            result.setVergino(pojo.getVergiNo());

            kitapRepository.save(result);

        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("*** KitapCsvPojo Eklenemedi. " + pojo.toString());
        }

        return result;
    }

    /**
     * @param pojos
     * @param uploadId
     */
    @Transactional
    public void save(List<KitapCsvPojo> pojos, Long uploadId) {
        if (pojos == null || pojos.size() == 0) {
            throw new NullPointerException("KitapCsvPojo listesi NULL olamaz!");
        }

        for (KitapCsvPojo pojo : pojos) {
            save(pojo, uploadId);
        }
    }

    /**
     * @param filePath
     * @param fileName
     * @param uploadId
     * @throws CsvParseServiceException
     */
    public void parse(String filePath, String fileName, Long uploadId) throws CsvParseServiceException {
        List<KitapCsvPojo> pojos = csvParseService.parse(filePath, fileName, UploadFileTypes.KITAP);
        save(pojos, uploadId);
    }
}
