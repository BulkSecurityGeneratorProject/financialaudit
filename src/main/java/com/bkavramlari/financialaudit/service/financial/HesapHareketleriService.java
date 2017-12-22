package com.bkavramlari.financialaudit.service.financial;

import com.bkavramlari.financialaudit.domain.enums.UploadFileTypes;
import com.bkavramlari.financialaudit.domain.financial.Buffer;
import com.bkavramlari.financialaudit.domain.financial.HesapHareketleri;
import com.bkavramlari.financialaudit.repository.financial.BufferRepository;
import com.bkavramlari.financialaudit.repository.financial.HesapHareketleriRepository;
import com.bkavramlari.financialaudit.service.financial.parser.csv.CsvParseService;
import com.bkavramlari.financialaudit.service.financial.parser.csv.exceptions.CsvParseServiceException;
import com.bkavramlari.financialaudit.service.financial.parser.csv.pojo.HesapHareketleriCsvPojo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by yildizib on 05/11/2017.
 */
@Data
@Slf4j
@Service
public class HesapHareketleriService {
    @Autowired
    private HesapHareketleriRepository hesapHareketleriRepository;
    @Autowired
    private CsvParseService csvParseService;
    @Autowired
    private BufferRepository bufferRepository;

    /**
     * @param pojo
     * @param uploadId
     * @return
     */
    @Transactional
    public HesapHareketleri save(HesapHareketleriCsvPojo pojo, Long uploadId) {
        HesapHareketleri result = null;
        if (pojo == null) {
            throw new NullPointerException("HesapPlaniCsvPojo NULL olamaz!");
        }

        try {

            result = new HesapHareketleri();

            result.setAy(pojo.getAy());
            result.setFistarihi(pojo.getFisTarihi());
            result.setYevmiyeno(Long.valueOf(pojo.getYevmiyeNo()));
            result.setKebirkodu(pojo.getKebirKodu());
            result.setKebiradi(pojo.getKebirAdi());
            result.setMuhasebehesapkodu(pojo.getMuhasebeHesapKodu());
            result.setMuhasebehesapadi(pojo.getMuhasebeHesapAdi());
            result.setBorc(Double.valueOf(pojo.getBorc()));
            result.setAlacak(Double.valueOf(pojo.getAlacak()));
            result.setAciklama(pojo.getAciklama());
            result.setUploadid(uploadId);

            hesapHareketleriRepository.save(result);

        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("*** HesapHareketleriCsvPojo Eklenemedi. " + pojo.toString());
        }

        return result;
    }

    /**
     * @param pojos
     * @param uploadId
     */
    @Transactional
    public void save(List<HesapHareketleriCsvPojo> pojos, Long uploadId) {
        if (pojos == null || pojos.size() == 0) {
            throw new NullPointerException("HesapHareketleriCsvPojo listesi NULL olamaz!");
        }

        for (HesapHareketleriCsvPojo pojo : pojos) {
            save(pojo, uploadId);
        }

                /* BUFFER */
        List<String> kebirKodlari = new LinkedList<>();
        kebirKodlari.add("191");
        kebirKodlari.add("320");
        kebirKodlari.add("391");
        kebirKodlari.add("120");

        List<Buffer> buffers = hesapHareketleriRepository.getBufferValues(uploadId, kebirKodlari);
        if (buffers != null) {
            for (Buffer b : buffers) {
                bufferRepository.save(b);
            }
        }
    }

    /**
     * @param filePath
     * @param fileName
     * @param uploadId
     * @throws CsvParseServiceException
     */
    public void parse(String filePath, String fileName, Long uploadId) throws CsvParseServiceException {
        List<HesapHareketleriCsvPojo> pojos = csvParseService.parse(filePath, fileName, UploadFileTypes.HESAP_HAREKETLERI);
        save(pojos, uploadId);
    }
}
