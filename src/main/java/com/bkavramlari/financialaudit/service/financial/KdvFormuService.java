package com.bkavramlari.financialaudit.service.financial;

import com.bkavramlari.financialaudit.domain.enums.UploadFileTypes;
import com.bkavramlari.financialaudit.domain.financial.*;
import com.bkavramlari.financialaudit.repository.financial.*;
import com.bkavramlari.financialaudit.service.financial.parser.csv.exceptions.CsvParseServiceException;
import com.bkavramlari.financialaudit.service.financial.parser.xml.XmlParseService;
import com.bkavramlari.financialaudit.service.financial.parser.xml.exceptions.XmlParseServiceException;
import com.bkavramlari.financialaudit.service.financial.parser.xml.pojo.KdvFormu;
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
public class KdvFormuService {

    @Autowired
    private XmlParseService xmlParseService;
    @Autowired
    private BAGenelRepository baGenelRepository;
    @Autowired
    private KDVOzelRepository kdvOzelRepository;
    @Autowired
    private UploadService uploadService;
    @Autowired
    private TevkifatTablosuRepository tevkifatTablosuRepository;
    @Autowired
    private IndirimlerRepository indirimlerRepository;
    @Autowired
    private IndirilecekKDVRepository indirilecekKDVRepository;

    /**
     * @param pojo
     * @param uploadId
     * @return
     */
    @Transactional
    public void save(KdvFormu pojo, Long uploadId) {
        if (pojo == null) {
            throw new NullPointerException("KdvFormu NULL olamaz!");
        }

        try {

            /* BAGENEL */
            Long alimFormuUploadId = uploadService.getLastUploadByCurrentUser(UploadFileTypes.ALIM_FORMU);
            BAGenel baGenel = baGenelRepository.findByUploadid(alimFormuUploadId);

            if (baGenel == null) {
                throw new NullPointerException("Once AlimFormu Parse edilmelidir!");
            }

            /* KDVOZEL */
            KDVOzel kdvOzel = null;
            try {

                kdvOzel = new KDVOzel();
                kdvOzel.setVergitoplami(Double.valueOf(pojo.getOzel().getVergiToplami()));
                kdvOzel.setToplammatrah(Double.valueOf(pojo.getOzel().getToplamMatrah()));
                kdvOzel.setHesaplanankdv(Double.valueOf(pojo.getOzel().getHesaplananKDV()));
                kdvOzel.setIlaveedilecekkdv(Double.valueOf(pojo.getOzel().getIlaveEdilecekKDV()));
                kdvOzel.setToplamkdv(Double.valueOf(pojo.getOzel().getToplamKDV()));
                kdvOzel.setIndirimlertoplami(Double.valueOf(pojo.getOzel().getIndirimlerToplami()));
                kdvOzel.setIndirilecekkdvodtoplamkdv(Double.valueOf(pojo.getOzel().getIndirilecekKDVODToplamKDV()));
                kdvOzel.setIhracgerceklesmeyenteciledilemeyenkdv(Double.valueOf(pojo.getOzel().getIhracGerceklesmeyenTecilEdilemeyenKDV()));
                kdvOzel.setKdvsizteminedilenlericinodenmeyenkdv(Double.valueOf(pojo.getOzel().getKdvsizTeminEdilenlerIcinOdenmeyenKDV()));
                kdvOzel.setIhracatdonemindeiadeedilecekkdv(Double.valueOf(pojo.getOzel().getIhracatDonemindeIadeEdilecekKDV()));
                kdvOzel.setIhracvergifark(Double.valueOf(pojo.getOzel().getIhracVergiFark()));
                kdvOzel.setYuklenilenkdv(Double.valueOf(pojo.getOzel().getYuklenilenKDV()));
                kdvOzel.setTeciledilecekkdv(Double.valueOf(pojo.getOzel().getTecilEdilecekKDV()));
                kdvOzel.setOdenmesigerekenkdv(Double.valueOf(pojo.getOzel().getOdenmesiGerekenKDV()));
                kdvOzel.setIadeedilmesigerekenkdv(Double.valueOf(pojo.getOzel().getIadeEdilmesiGerekenKDV()));
                kdvOzel.setSonrakidonemedevredenkdv(Double.valueOf(pojo.getOzel().getSonrakiDonemeDevredenKDV()));
                kdvOzel.setMatrahadahilolmayanbedel(Double.valueOf(pojo.getOzel().getMatrahaDahilOlmayanBedel()));
                kdvOzel.setTeslimvehizmetleriteskiledenbedelaylik(Double.valueOf(pojo.getOzel().getTeslimVeHizmetleriTeskilEdenBedelAylik()));
                kdvOzel.setTeslimvehizmetleriteskiledenbedelkumulatif(Double.valueOf(pojo.getOzel().getTeslimVeHizmetleriTeskilEdenBedelKumulatif()));
                kdvOzel.setKredikarti(Double.valueOf(pojo.getOzel().getKrediKarti()));
                kdvOzel.setUploadid(uploadId);

                kdvOzelRepository.save(kdvOzel);

            } catch (Exception e) {
                log.error(e.getMessage());
            }

            if (kdvOzel == null) {
                throw new NullPointerException("KDV OZEL Id NULL!");
            }

            /* TEVKIFAT */
            if (pojo.getOzel().getTevkifatUygulanmayanlar() != null
                    && pojo.getOzel().getTevkifatUygulanmayanlar().getTevkifatUygulanmayan() != null) {

                for (KdvFormu.TevkifatUygulanmayan tu : pojo.getOzel().getTevkifatUygulanmayanlar().getTevkifatUygulanmayan()) {
                    try {

                        TevkifatTablosu tt = new TevkifatTablosu();
                        tt.setKdvozelid(kdvOzel.getId());
                        tt.setUploadid(uploadId);
                        tt.setMatrah(Double.valueOf(tu.getMatrah()));
                        tt.setOran(Long.valueOf(tu.getOran()));
                        tt.setVergi(Double.valueOf(tu.getVergi()));
                        tt.setBagenelid(baGenel.getId());

                        tevkifatTablosuRepository.save(tt);

                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                }

            }

            /* INDIRIMLER */
            if (pojo.getOzel().getIndirimler() != null
                    && pojo.getOzel().getIndirimler().getIndirim() != null) {

                for (KdvFormu.Indirim indirim : pojo.getOzel().getIndirimler().getIndirim()) {
                    try {

                        Indirimler indirimler = new Indirimler();
                        indirimler.setIndirimturu(indirim.getIndirimTuru());
                        indirimler.setVergi(Double.valueOf(indirim.getVergi()));
                        indirimler.setKdvozelid(kdvOzel.getId());

                        indirimlerRepository.save(indirimler);

                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                }

            }

            /* INDIRILECEK_KDV */
            if (pojo.getOzel().getIndirilecekKDVODler() != null
                    && pojo.getOzel().getIndirilecekKDVODler().getIndirilecekKDVOD() != null) {

                for (KdvFormu.IndirilecekKDVOD ikdvod : pojo.getOzel().getIndirilecekKDVODler().getIndirilecekKDVOD()) {
                    try {

                        IndirilecekKDV indirilecekKDV = new IndirilecekKDV();
                        indirilecekKDV.setOran(ikdvod.getOran() != null ? Long.valueOf(ikdvod.getOran()) : null);
                        indirilecekKDV.setBedel(Double.valueOf(ikdvod.getBedel()));
                        indirilecekKDV.setKdvtutari(Double.valueOf(ikdvod.getKDVTutari()));
                        indirilecekKDV.setKdvozelid(kdvOzel.getId());
                        indirilecekKDV.setUploadid(uploadId);
                        indirilecekKDV.setBagenelid(baGenel.getId());

                        indirilecekKDVRepository.save(indirilecekKDV);

                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                }

            }


        } catch (Exception e) {
            log.error(e.getMessage());
            log.error("*** KdvFormu Eklenemedi. " + pojo.toString());
        }
    }

    /**
     * @param pojos
     * @param uploadId
     */
    @Transactional
    public void save(List<KdvFormu> pojos, Long uploadId) {
        if (pojos == null || pojos.size() == 0) {
            throw new NullPointerException("KdvFormu listesi NULL olamaz!");
        }

        for (KdvFormu pojo : pojos) {
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
        KdvFormu pojos = xmlParseService.parse(filePath, fileName, UploadFileTypes.KDV);
        save(pojos, uploadId);
    }
}
