package com.bkavramlari.financialaudit.service.financial;

import com.bkavramlari.financialaudit.domain.enums.UploadFileTypes;
import com.bkavramlari.financialaudit.domain.financial.HesapPlani;
import com.bkavramlari.financialaudit.repository.financial.HesapPlaniRepository;
import com.bkavramlari.financialaudit.service.financial.parser.csv.CsvParseService;
import com.bkavramlari.financialaudit.service.financial.parser.csv.exceptions.CsvParseServiceException;
import com.bkavramlari.financialaudit.service.financial.parser.csv.pojo.HesapPlaniCsvPojo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
public class HesapPlaniService {
    @Autowired
    private HesapPlaniRepository hesapPlaniRepository;
    @Autowired
    private CsvParseService csvParseService;

    /**
     * @param pojo
     * @param uploadId
     * @return
     */
    @Transactional
    public HesapPlani save(HesapPlaniCsvPojo pojo, Long uploadId) {
        HesapPlani result = null;
        if (pojo == null) {
            throw new NullPointerException("HesapPlaniCsvPojo NULL olamaz!");
        }

        try {

            result = new HesapPlani();

            result.setAktifpasif(pojo.getAktifPasif());
            result.setAlacakbakiye((StringUtils.trimToNull(pojo.getAlacakBakiye()) != null ? Long.valueOf(pojo.getAlacakBakiye()) : null));
            result.setBilancotablosu((StringUtils.trimToNull(pojo.getBilancoTablosu()) != null ? Long.valueOf(pojo.getBilancoTablosu()) : null));
            result.setBorcbakiye((StringUtils.trimToNull(pojo.getBorcBakiye()) != null ? Long.valueOf(pojo.getBorcBakiye()) : null));
            result.setDovizcinsi(pojo.getDovizCinsi());
            result.setGelirtablosu((StringUtils.trimToNull(pojo.getGelirTablosu()) != null ? Long.valueOf(pojo.getGelirTablosu()) : null));
            result.setGidertablosu((StringUtils.trimToNull(pojo.getGiderTablosu()) != null ? Long.valueOf(pojo.getGiderTablosu()) : null));
            result.setGrup1(pojo.getGrup1());
            result.setGrup1yabancidil((StringUtils.trimToNull(pojo.getGrup1YabanciDil()) != null ? Long.valueOf(pojo.getGrup1YabanciDil()) : null));
            result.setGrup2(pojo.getGrup2());
            result.setGrup2yabancidil((StringUtils.trimToNull(pojo.getGrup2YabanciDil()) != null ? Long.valueOf(pojo.getGrup2YabanciDil()) : null));
            result.setGrup3(pojo.getGrup3());
            result.setGrup3yabancidil((StringUtils.trimToNull(pojo.getGrup3YabanciDil()) != null ? Long.valueOf(pojo.getGrup3YabanciDil()) : null));
            result.setGrup4(pojo.getGrup4());
            result.setGrup4yabancidil((StringUtils.trimToNull(pojo.getGrup4YabanciDil()) != null ? Long.valueOf(pojo.getGrup4YabanciDil()) : null));
            result.setHesapadi(pojo.getHesapAdi());
            result.setHesapadiyabancidil((StringUtils.trimToNull(pojo.getHesapAdiYabanciDil()) != null ? Long.valueOf(pojo.getHesapAdiYabanciDil()) : null));
            result.setHesapkodu(pojo.getHesapKodu());
            result.setNakittablosu((StringUtils.trimToNull(pojo.getNakitTablosu()) != null ? Long.valueOf(pojo.getNakitTablosu()) : null));
            result.setNazimhesap((StringUtils.trimToNull(pojo.getNazimHesap()) != null ? Long.valueOf(pojo.getNazimHesap()) : null));
            result.setOzel1((StringUtils.trimToNull(pojo.getOzel1()) != null ? Long.valueOf(pojo.getOzel1()) : null));
            result.setOzel2((StringUtils.trimToNull(pojo.getOzel2()) != null ? Long.valueOf(pojo.getOzel2()) : null));
            result.setOzeltablosu((StringUtils.trimToNull(pojo.getOzelTablosu()) != null ? Long.valueOf(pojo.getOzelTablosu()) : null));
            result.setSenekapatmahesabi((StringUtils.trimToNull(pojo.getSeneKapamaHesabi()) != null ? Long.valueOf(pojo.getSeneKapamaHesabi()) : null));
            result.setSenekapatmasirasi((StringUtils.trimToNull(pojo.getSeneKapamaSirasi()) != null ? Long.valueOf(pojo.getSeneKapamaSirasi()) : null));
            result.setUploadid(uploadId);

            hesapPlaniRepository.save(result);

        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("*** HesapPlaniCsvPojo Eklenemedi. " + pojo.toString());
        }

        return result;
    }

    /**
     * @param pojos
     * @param uploadId
     */
    @Transactional
    public void save(List<HesapPlaniCsvPojo> pojos, Long uploadId) {
        if (pojos == null || pojos.size() == 0) {
            throw new NullPointerException("HesapPlaniCsvPojo listesi NULL olamaz!");
        }

        for (HesapPlaniCsvPojo pojo : pojos) {
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
        List<HesapPlaniCsvPojo> pojos = csvParseService.parse(filePath, fileName, UploadFileTypes.HESAP_PLANI);
        save(pojos, uploadId);
    }
}
