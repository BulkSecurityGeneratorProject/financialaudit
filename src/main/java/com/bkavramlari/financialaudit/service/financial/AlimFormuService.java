package com.bkavramlari.financialaudit.service.financial;

import com.bkavramlari.financialaudit.domain.enums.UploadFileTypes;
import com.bkavramlari.financialaudit.domain.financial.BAGenel;
import com.bkavramlari.financialaudit.domain.financial.BAOzel;
import com.bkavramlari.financialaudit.domain.financial.FirmaGenel;
import com.bkavramlari.financialaudit.repository.financial.BAGenelRepository;
import com.bkavramlari.financialaudit.repository.financial.BAOzelRepository;
import com.bkavramlari.financialaudit.repository.financial.FirmaGenelRepository;
import com.bkavramlari.financialaudit.service.financial.parser.csv.exceptions.CsvParseServiceException;
import com.bkavramlari.financialaudit.service.financial.parser.xml.XmlParseService;
import com.bkavramlari.financialaudit.service.financial.parser.xml.exceptions.XmlParseServiceException;
import com.bkavramlari.financialaudit.service.financial.parser.xml.pojo.AlimFormu;
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
public class AlimFormuService {

    @Autowired
    private XmlParseService xmlParseService;
    @Autowired
    private BAGenelRepository baGenelRepository;
    @Autowired
    private BAOzelRepository baOzelRepository;
    @Autowired
    private FirmaGenelRepository firmaGenelRepository;

    /**
     * @param pojo
     * @param uploadId
     * @return
     */
    @Transactional
    public void save(AlimFormu pojo, Long uploadId) {
        if (pojo == null) {
            throw new NullPointerException("AlimFormu NULL olamaz!");
        }

        try {
            /* BAGENEL */
            BAGenel baGenel = new BAGenel();
            baGenel.setDonemay(Byte.valueOf(pojo.getGenel().getIdari().getDonem().getAy()));
            baGenel.setDonemtip(pojo.getGenel().getIdari().getDonem().getTip());
            baGenel.setDonemyil(Short.valueOf(pojo.getGenel().getIdari().getDonem().getYil()));
            baGenel.setDuzenleyen(pojo.getGenel().getDuzenleyen().getVergiNo());
            baGenel.setFgid(pojo.getGenel().getMukellef().getVergiNo());
            baGenel.setGonderen(pojo.getGenel().getGonderen().getVergiNo());
            baGenel.setHsv(pojo.getGenel().getHsv().getVergiNo());
            baGenel.setMukellef(pojo.getGenel().getMukellef().getVergiNo());
            baGenel.setVdkodu(pojo.getGenel().getIdari().getVdKodu());
            baGenel.setUploadid(uploadId);
            baGenel.setYmm(pojo.getGenel().getYmm().getVergiNo());

            baGenelRepository.save(baGenel);

            /* BAOZEL */
            if (pojo.getOzel() != null && pojo.getOzel().getAlisBildirimi() != null
                    && pojo.getOzel().getAlisBildirimi().getBa() != null) {

                for (AlimFormu.BA ba : pojo.getOzel().getAlisBildirimi().getBa()) {

                    try {

                        BAOzel baOzel = new BAOzel();

                        baOzel.setBagenelid(baGenel.getId());
                        baOzel.setBelgesayisi(StringUtils.trimToNull(ba.getBelgeSayisi()) != null ? Long.valueOf(ba.getBelgeSayisi()) : null);
                        baOzel.setFoid(ba.getVkno() != null ? ba.getVkno() : ba.getTckimlikno());
                        baOzel.setMalhizmetbedeli(StringUtils.trimToNull(ba.getMalHizmetBedeli()) != null ? Double.valueOf(ba.getMalHizmetBedeli()) : null);
                        baOzel.setSirano(ba.getSiraNo() != null ? Long.valueOf(ba.getSiraNo()) : null);
                        baOzel.setUnvan(ba.getUnvan());
                        baOzel.setVkNo(ba.getVkno() != null ? ba.getVkno() : ba.getTckimlikno());
                        baOzel.setUlke(ba.getUlke());
                        baOzel.setUploadid(uploadId);

                        baOzelRepository.save(baOzel);

                    } catch (Exception e) {
                        log.error(e.getMessage());
                        log.error("BAOZEL e eklenemedi. Pojo: " + pojo.toString());
                    }
                }

            }

            /* MUKELLEF */
            FirmaGenel firmaGenel = new FirmaGenel();
            firmaGenel.setVergino(pojo.getGenel().getMukellef().getVergiNo());
            firmaGenel.setSoyadi(pojo.getGenel().getMukellef().getSoyadi());
            firmaGenel.setAdi(pojo.getGenel().getMukellef().getAdi());
            firmaGenel.setAlankodu(StringUtils.trimToNull(pojo.getGenel().getMukellef().getAlanKodu()) != null ?
                    Long.valueOf(pojo.getGenel().getMukellef().getAlanKodu()) : null);
            firmaGenel.setEposta(pojo.getGenel().getMukellef().getEposta());
            firmaGenel.setTelno(StringUtils.trimToNull(pojo.getGenel().getMukellef().getTelNo()) != null ?
                    Long.valueOf(pojo.getGenel().getMukellef().getTelNo()) : null);

            firmaGenelRepository.save(firmaGenel);

            /* YMM */
            firmaGenel = new FirmaGenel();
            firmaGenel.setVergino(pojo.getGenel().getYmm().getVergiNo());
            firmaGenel.setSoyadi(pojo.getGenel().getYmm().getSoyadi());
            firmaGenel.setAdi(pojo.getGenel().getYmm().getAdi());
            firmaGenel.setAlankodu(StringUtils.trimToNull(pojo.getGenel().getYmm().getAlanKodu()) != null ?
                    Long.valueOf(pojo.getGenel().getYmm().getAlanKodu()) : null);
            firmaGenel.setEposta(pojo.getGenel().getYmm().getEposta());
            firmaGenel.setTelno(StringUtils.trimToNull(pojo.getGenel().getYmm().getTelNo()) != null ?
                    Long.valueOf(pojo.getGenel().getYmm().getTelNo()) : null);

            firmaGenelRepository.save(firmaGenel);

        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("*** AlimFormu Eklenemedi. " + pojo.toString());
        }
    }

    /**
     * @param pojos
     * @param uploadId
     */
    @Transactional
    public void save(List<AlimFormu> pojos, Long uploadId) {
        if (pojos == null || pojos.size() == 0) {
            throw new NullPointerException("AlimFormu listesi NULL olamaz!");
        }

        for (AlimFormu pojo : pojos) {
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
        AlimFormu pojos = xmlParseService.parse(filePath, fileName, UploadFileTypes.ALIM_FORMU);
        save(pojos, uploadId);
    }
}
