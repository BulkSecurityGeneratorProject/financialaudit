package com.bkavramlari.financialaudit.service.financial;

import com.bkavramlari.financialaudit.domain.enums.UploadFileTypes;
import com.bkavramlari.financialaudit.domain.financial.BAGenel;
import com.bkavramlari.financialaudit.domain.financial.BSOzel;
import com.bkavramlari.financialaudit.repository.financial.BAGenelRepository;
import com.bkavramlari.financialaudit.repository.financial.BSOzelRepository;
import com.bkavramlari.financialaudit.service.financial.parser.csv.exceptions.CsvParseServiceException;
import com.bkavramlari.financialaudit.service.financial.parser.xml.XmlParseService;
import com.bkavramlari.financialaudit.service.financial.parser.xml.exceptions.XmlParseServiceException;
import com.bkavramlari.financialaudit.service.financial.parser.xml.pojo.SatisFormu;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by yildizib on 05/11/2017.
 */
@Data
@Slf4j
@Service
public class SatisFormuService {

    @Autowired
    private XmlParseService xmlParseService;
    @Autowired
    private BAGenelRepository baGenelRepository;
    @Autowired
    private BSOzelRepository bsOzelRepository;
    @Autowired
    private UploadService uploadService;

    /**
     * @param pojo
     * @param uploadId
     * @return
     */
    @Transactional
    public void save(SatisFormu pojo, Long uploadId) {
        if (pojo == null) {
            throw new NullPointerException("SatisFormu NULL olamaz!");
        }

        try {

            /* BAGENEL */
            Long alimFormuUploadId = uploadService.getLastUploadByCurrentUser(UploadFileTypes.ALIM_FORMU);
            BAGenel baGenel = baGenelRepository.findByUploadid(alimFormuUploadId);

            if (baGenel == null) {
                throw new NullPointerException("Once AlimFormu Parse edilmelidir!");
            }

            /* BSOZEL */
            if (pojo.getOzel() != null && pojo.getOzel().getSatisBildirimi() != null
                    && pojo.getOzel().getSatisBildirimi().getBs() != null) {

                for (SatisFormu.BS bs : pojo.getOzel().getSatisBildirimi().getBs()) {

                    try {

                        BSOzel baOzel = new BSOzel();

                        baOzel.setBagenelid(baGenel.getId());
                        baOzel.setBelgesayisi(bs.getBelgeSayisi() != null ? Long.valueOf(bs.getBelgeSayisi()) : null);
                        baOzel.setFoid(bs.getVkno() != null ? bs.getVkno() : bs.getTckimlikno());
                        baOzel.setMalhizmetbedeli(StringUtils.trimToNull(bs.getMalHizmetBedeli()) != null ? Double.valueOf(bs.getMalHizmetBedeli()) : null);
                        baOzel.setSirano(StringUtils.trimToNull(bs.getSiraNo()) != null ? Long.valueOf(bs.getSiraNo()) : null);
                        baOzel.setUnvan(bs.getUnvan());
                        baOzel.setVkNo(bs.getVkno() != null ? bs.getVkno() : bs.getTckimlikno());
                        baOzel.setUlke(bs.getUlke());
                        baOzel.setUploadid(uploadId);

                        bsOzelRepository.save(baOzel);

                    } catch (Exception e) {
                        log.error(e.getMessage());
                        log.error("BSOZel e eklenemdi. pojo:" + pojo.toString());
                    }

                }

            }

        } catch (DataIntegrityViolationException e) {
            log.error(e.getMessage());
            log.error("*** SatisFormu Eklenemedi. " + pojo.toString());
        }
    }

    /**
     * @param pojos
     * @param uploadId
     */
    @Transactional
    public void save(List<SatisFormu> pojos, Long uploadId) {
        if (pojos == null || pojos.size() == 0) {
            throw new NullPointerException("SatisFormu listesi NULL olamaz!");
        }

        for (SatisFormu pojo : pojos) {
            save(pojo, uploadId);
        }
    }

    /**
     * @param filePath
     * @param fileName
     * @param uploadId
     * @throws CsvParseServiceException
     */
    public void parse(String filePath, String fileName, Long uploadId) throws XmlParseServiceException {
        SatisFormu pojos = xmlParseService.parse(filePath, fileName, UploadFileTypes.SATIS_FORMU);
        save(pojos, uploadId);
    }
}
