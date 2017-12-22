package com.bkavramlari.financialaudit.service.financial;

import com.bkavramlari.financialaudit.config.CacheConfig;
import com.bkavramlari.financialaudit.domain.dto.KdvAnalizCevapDTO;
import com.bkavramlari.financialaudit.domain.financial.*;
import com.bkavramlari.financialaudit.repository.financial.*;
import com.bkavramlari.financialaudit.service.UserService;
import com.bkavramlari.financialaudit.service.financial.exceptions.KdvAnalizException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * KDV analizi islemleri - "islem5.php"
 * <p>
 * Created by yildizib on 06/11/2017.
 */
@Data
@Slf4j
@Service
public class KdvAnalizService extends AnalizBaseService {

    @Autowired
    private HesapPlaniRepository hesapPlaniRepository;
    @Autowired
    private HesapHareketleriRepository hesapHareketleriRepository;
    @Autowired
    private FirmaGenelRepository firmaGenelRepository;
    @Autowired
    private IndirilecekKDVRepository indirilecekKDVRepository;
    @Autowired
    private BAGenelRepository baGenelRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private UserService userService;
    @Autowired
    private TevkifatTablosuRepository tevkifatTablosuRepository;
    @Autowired
    private KDVOzelRepository kdvOzelRepository;

    /**
     * @param hesapPlaniUploadId
     * @param hesapHareketleriUploadId
     * @return
     */
    public Double yuzdeBirKdvBorcToplamiHesapla(Long hesapPlaniUploadId, Long hesapHareketleriUploadId) throws KdvAnalizException {
        return kdvBorcToplamiHesapla(hesapPlaniUploadId, hesapHareketleriUploadId, "%1");
    }

    /**
     * KDV borclar genel toplami
     *
     * @param hesapPlaniUploadId
     * @param hesapHareketleriUploadId
     * @return
     * @throws KdvAnalizException
     */
    public Double kdvBorclarGenelToplam(Long hesapPlaniUploadId, Long hesapHareketleriUploadId) throws KdvAnalizException {
        Double result = 0D;
        Double yuzdeBirBorcToplami = yuzdeBirKdvBorcToplamiHesapla(hesapPlaniUploadId, hesapHareketleriUploadId);
        Double yuzdeSekizBorcToplami = yuzdeSekizKdvBorcToplamiHesapla(hesapPlaniUploadId, hesapHareketleriUploadId);
        Double yuzdeOnSekizBorcToplami = yuzdeOnSekizKdvBorcToplamiHesapla(hesapPlaniUploadId, hesapHareketleriUploadId);

        result = yuzdeBirBorcToplami + yuzdeSekizBorcToplami + yuzdeOnSekizBorcToplami;
        return result;
    }

    /**
     * KDV alacaklar Genel Toplami
     *
     * @param hesapPlaniUploadId
     * @param hesapHareketleriUploadId
     * @return
     * @throws KdvAnalizException
     */
    public Double kdvAlacaklarGenelToplam(Long hesapPlaniUploadId, Long hesapHareketleriUploadId) throws KdvAnalizException {
        Double result = 0D;
        Double yuzdeBirAlacakToplami = yuzdeBirKdvAlacakToplamiHesapla(hesapPlaniUploadId, hesapHareketleriUploadId);
        Double yuzdeSekizAlacakToplami = yuzdeSekizKdvAlacakToplamiHesapla(hesapPlaniUploadId, hesapHareketleriUploadId);
        Double yuzdeOnSekizAlacakToplami = yuzdeOnSekizKdvAlacakToplamiHesapla(hesapPlaniUploadId, hesapHareketleriUploadId);

        result = yuzdeBirAlacakToplami + yuzdeSekizAlacakToplami + yuzdeOnSekizAlacakToplami;
        return result;
    }

    /**
     * @param hesapPlaniUploadId
     * @param hesapHareketleriUploadId
     * @return
     * @throws KdvAnalizException
     */
    public Double yuzdeBirKdvAlacakToplamiHesapla(Long hesapPlaniUploadId, Long hesapHareketleriUploadId) throws KdvAnalizException {
        return kdvAlacakToplamiHesapla(hesapPlaniUploadId, hesapHareketleriUploadId, "%1");
    }

    /**
     * @param hesapPlaniUploadId
     * @param hesapHareketleriUploadId
     * @return
     * @throws KdvAnalizException
     */
    public Double yuzdeSekizKdvBorcToplamiHesapla(Long hesapPlaniUploadId, Long hesapHareketleriUploadId) throws KdvAnalizException {
        return kdvBorcToplamiHesapla(hesapPlaniUploadId, hesapHareketleriUploadId, "%8");
    }

    /**
     * @param hesapPlaniUploadId
     * @param hesapHareketleriUploadId
     * @return
     * @throws KdvAnalizException
     */
    public Double yuzdeSekizKdvAlacakToplamiHesapla(Long hesapPlaniUploadId, Long hesapHareketleriUploadId) throws KdvAnalizException {
        return kdvAlacakToplamiHesapla(hesapPlaniUploadId, hesapHareketleriUploadId, "%8");
    }

    /**
     * @param hesapPlaniUploadId
     * @param hesapHareketleriUploadId
     * @return
     * @throws KdvAnalizException
     */
    public Double yuzdeOnSekizKdvBorcToplamiHesapla(Long hesapPlaniUploadId, Long hesapHareketleriUploadId) throws KdvAnalizException {
        return kdvBorcToplamiHesapla(hesapPlaniUploadId, hesapHareketleriUploadId, "%18");
    }

    /**
     * @param hesapPlaniUploadId
     * @param hesapHareketleriUploadId
     * @return
     * @throws KdvAnalizException
     */
    public Double yuzdeOnSekizKdvAlacakToplamiHesapla(Long hesapPlaniUploadId, Long hesapHareketleriUploadId) throws KdvAnalizException {
        return kdvAlacakToplamiHesapla(hesapPlaniUploadId, hesapHareketleriUploadId, "%18");
    }

    /**
     * @return
     */
    @Cacheable(cacheNames = CacheConfig.ICacheNames.DAILY)
    public List<KdvAnalizCevapDTO> analizEt(String loginName, Long lastUploadByCurrentUser, Pageable pageable) throws KdvAnalizException {
        List<KdvAnalizCevapDTO> dto = new LinkedList<>();
        Map<Tables, Long> uploadIdsByTableName = getUploadIdsByTableName();
        Long hesapPlaniUploadId = uploadIdsByTableName.get(Tables.HESAPPLANI);
        Long hesapHareketleriUploadId = uploadIdsByTableName.get(Tables.HESAPHAREKETLERI);
        Long indirilecekKDVUploadId = uploadIdsByTableName.get(Tables.INDIRILECEKKDV);
        Long indirilecekKDVAlacakUploadId = uploadIdsByTableName.get(Tables.TEVKIFATTABLOSU);
        Long kdvOzelUploadId = uploadIdsByTableName.get(Tables.KDVOZEL);
        List<KdvAnalizCevapDTO> analizSonuclari = new LinkedList<>();
        Locale locale = Locale.forLanguageTag(userService.getUserWithAuthorities().get().getLangKey());
        String negativeMessageTemplate = messageSource.getMessage("kdvanaliz.negative.message", null, locale);
        String positiveMessageTemlate = messageSource.getMessage("kdvanaliz.positive.message", null, locale);

        // BORC ICIN
        List<String> hesapKodlari = new LinkedList<>();
        hesapKodlari.add("191");
        hesapKodlari.add("192");
        KdvAnalizCevapDTO.AnalizToplamTipi toplamTipi = KdvAnalizCevapDTO.AnalizToplamTipi.HESAP_KODU_BORC;
        KdvAnalizCevapDTO borcAnalizSonuclari = null;

        // %1 KDV
        Double yuzdeBirKdvBorcToplam = yuzdeBirKdvBorcToplamiHesapla(hesapPlaniUploadId, hesapHareketleriUploadId);
        Map<String, Object> yuzdeBirBorcIndKDV = indirilecekKDVOranIleGetirBorcIcin(indirilecekKDVUploadId, 1L);
        borcAnalizSonuclari = new KdvAnalizCevapDTO();

        borcAnalizSonuclari.setTip(toplamTipi);
        borcAnalizSonuclari.setHesapKodlari(hesapKodlari);
        borcAnalizSonuclari.setBorcTutari(yuzdeBirKdvBorcToplam);
        borcAnalizSonuclari.setKdvOrani(1L);
        borcAnalizSonuclari.setFirmaAdi((String) yuzdeBirBorcIndKDV.get("FIRMA_ADI"));
        borcAnalizSonuclari.setKdvTutari((Double) yuzdeBirBorcIndKDV.get("KDV_TUTARI"));
        borcAnalizSonuclari.setVergiNo((String) yuzdeBirBorcIndKDV.get("FIRMA_VERGI_NO"));
        borcAnalizSonuclari.setSonuc(
                (
                        borcAnalizSonuclari.getBorcTutari().doubleValue() != borcAnalizSonuclari.getKdvTutari().doubleValue() ?
                                KdvAnalizCevapDTO.AnalizDurumlari.TUTARLAR_FARKLI : KdvAnalizCevapDTO.AnalizDurumlari.TUTARLAR_ESIT
                )
        );
        borcAnalizSonuclari.setMesaj(
                KdvAnalizCevapDTO.AnalizDurumlari.TUTARLAR_ESIT.equals(borcAnalizSonuclari.getSonuc()) ?
                        positiveMessageTemlate
                                .replace("{FIRMA}", borcAnalizSonuclari.getFirmaAdi())
                                .replace("{KDV_ORAN}", borcAnalizSonuclari.getKdvOrani() + "")
                        :
                        negativeMessageTemplate
                                .replace("{FIRMA}", borcAnalizSonuclari.getFirmaAdi())
                                .replace("{KDV_ORAN}", borcAnalizSonuclari.getKdvOrani() + "")
                                .replace("{TOPLAM}", borcAnalizSonuclari.getBorcTutari() + "")
                                .replace("{KDV_TOPLAM}", borcAnalizSonuclari.getKdvTutari() + "")
        );

        dto.add(borcAnalizSonuclari);

        // %8 KDV
        Double yuzdeSekizKdvBorcToplam = yuzdeSekizKdvBorcToplamiHesapla(hesapPlaniUploadId, hesapHareketleriUploadId);
        Map<String, Object> yuzdeSekizBorcIndKDV = indirilecekKDVOranIleGetirBorcIcin(indirilecekKDVUploadId, 8L);
        borcAnalizSonuclari = new KdvAnalizCevapDTO();

        borcAnalizSonuclari.setTip(toplamTipi);
        borcAnalizSonuclari.setHesapKodlari(hesapKodlari);
        borcAnalizSonuclari.setBorcTutari(yuzdeSekizKdvBorcToplam);
        borcAnalizSonuclari.setKdvOrani(8L);
        borcAnalizSonuclari.setFirmaAdi((String) yuzdeSekizBorcIndKDV.get("FIRMA_ADI"));
        borcAnalizSonuclari.setKdvTutari((Double) yuzdeSekizBorcIndKDV.get("KDV_TUTARI"));
        borcAnalizSonuclari.setVergiNo((String) yuzdeSekizBorcIndKDV.get("FIRMA_VERGI_NO"));
        borcAnalizSonuclari.setSonuc(
                (
                        borcAnalizSonuclari.getBorcTutari().doubleValue() != borcAnalizSonuclari.getKdvTutari().doubleValue() ?
                                KdvAnalizCevapDTO.AnalizDurumlari.TUTARLAR_FARKLI : KdvAnalizCevapDTO.AnalizDurumlari.TUTARLAR_ESIT
                )
        );
        borcAnalizSonuclari.setMesaj(
                KdvAnalizCevapDTO.AnalizDurumlari.TUTARLAR_ESIT.equals(borcAnalizSonuclari.getSonuc()) ?
                        positiveMessageTemlate
                                .replace("{FIRMA}", borcAnalizSonuclari.getFirmaAdi())
                                .replace("{KDV_ORAN}", borcAnalizSonuclari.getKdvOrani() + "")
                        :
                        negativeMessageTemplate
                                .replace("{FIRMA}", borcAnalizSonuclari.getFirmaAdi())
                                .replace("{KDV_ORAN}", borcAnalizSonuclari.getKdvOrani() + "")
                                .replace("{TOPLAM}", borcAnalizSonuclari.getBorcTutari() + "")
                                .replace("{KDV_TOPLAM}", borcAnalizSonuclari.getKdvTutari() + "")
        );

        dto.add(borcAnalizSonuclari);

        // %18 KDV
        Double yuzdeOnSekizKdvBorcToplam = yuzdeOnSekizKdvBorcToplamiHesapla(hesapPlaniUploadId, hesapHareketleriUploadId);
        Map<String, Object> yuzdeOnSekizBorcIndKDV = indirilecekKDVOranIleGetirBorcIcin(indirilecekKDVUploadId, 8L);
        borcAnalizSonuclari = new KdvAnalizCevapDTO();

        borcAnalizSonuclari.setTip(toplamTipi);
        borcAnalizSonuclari.setHesapKodlari(hesapKodlari);
        borcAnalizSonuclari.setBorcTutari(yuzdeOnSekizKdvBorcToplam);
        borcAnalizSonuclari.setKdvOrani(18L);
        borcAnalizSonuclari.setFirmaAdi((String) yuzdeOnSekizBorcIndKDV.get("FIRMA_ADI"));
        borcAnalizSonuclari.setKdvTutari((Double) yuzdeOnSekizBorcIndKDV.get("KDV_TUTARI"));
        borcAnalizSonuclari.setVergiNo((String) yuzdeOnSekizBorcIndKDV.get("FIRMA_VERGI_NO"));
        borcAnalizSonuclari.setSonuc(
                (
                        borcAnalizSonuclari.getBorcTutari().doubleValue() != borcAnalizSonuclari.getKdvTutari().doubleValue() ?
                                KdvAnalizCevapDTO.AnalizDurumlari.TUTARLAR_FARKLI : KdvAnalizCevapDTO.AnalizDurumlari.TUTARLAR_ESIT
                )
        );
        borcAnalizSonuclari.setMesaj(
                KdvAnalizCevapDTO.AnalizDurumlari.TUTARLAR_ESIT.equals(borcAnalizSonuclari.getSonuc()) ?
                        positiveMessageTemlate
                                .replace("{FIRMA}", borcAnalizSonuclari.getFirmaAdi())
                                .replace("{KDV_ORAN}", borcAnalizSonuclari.getKdvOrani() + "")
                        :
                        negativeMessageTemplate
                                .replace("{FIRMA}", borcAnalizSonuclari.getFirmaAdi())
                                .replace("{KDV_ORAN}", borcAnalizSonuclari.getKdvOrani() + "")
                                .replace("{TOPLAM}", borcAnalizSonuclari.getBorcTutari() + "")
                                .replace("{KDV_TOPLAM}", borcAnalizSonuclari.getKdvTutari() + "")
        );

        dto.add(borcAnalizSonuclari);

        // ALACAK ICIN
        hesapKodlari = new LinkedList<>();
        hesapKodlari.add("391");
        toplamTipi = KdvAnalizCevapDTO.AnalizToplamTipi.HESAP_KODU_ALACAK;

        // %1 KDV
        Double yuzdeBirKdvAlacakToplam = yuzdeBirKdvAlacakToplamiHesapla(hesapPlaniUploadId, hesapHareketleriUploadId);
        Map<String, Object> yuzdeBirAlacakIndKDV = indirilecekKDVOranIleGetirAlacakIcin(indirilecekKDVAlacakUploadId, 1L);
        borcAnalizSonuclari = new KdvAnalizCevapDTO();

        borcAnalizSonuclari.setTip(toplamTipi);
        borcAnalizSonuclari.setHesapKodlari(hesapKodlari);
        borcAnalizSonuclari.setBorcTutari(yuzdeBirKdvAlacakToplam);
        borcAnalizSonuclari.setKdvOrani(1L);
        borcAnalizSonuclari.setFirmaAdi((String) yuzdeBirAlacakIndKDV.get("FIRMA_ADI"));
        borcAnalizSonuclari.setKdvTutari((Double) yuzdeBirAlacakIndKDV.get("KDV_TUTARI"));
        borcAnalizSonuclari.setVergiNo((String) yuzdeBirAlacakIndKDV.get("FIRMA_VERGI_NO"));
        borcAnalizSonuclari.setSonuc(
                (
                        borcAnalizSonuclari.getBorcTutari().doubleValue() != borcAnalizSonuclari.getKdvTutari().doubleValue() ?
                                KdvAnalizCevapDTO.AnalizDurumlari.TUTARLAR_FARKLI : KdvAnalizCevapDTO.AnalizDurumlari.TUTARLAR_ESIT
                )
        );
        borcAnalizSonuclari.setMesaj(
                KdvAnalizCevapDTO.AnalizDurumlari.TUTARLAR_ESIT.equals(borcAnalizSonuclari.getSonuc()) ?
                        positiveMessageTemlate
                                .replace("{FIRMA}", borcAnalizSonuclari.getFirmaAdi())
                                .replace("{KDV_ORAN}", borcAnalizSonuclari.getKdvOrani() + "")
                        :
                        negativeMessageTemplate
                                .replace("{FIRMA}", borcAnalizSonuclari.getFirmaAdi())
                                .replace("{KDV_ORAN}", borcAnalizSonuclari.getKdvOrani() + "")
                                .replace("{TOPLAM}", borcAnalizSonuclari.getBorcTutari() + "")
                                .replace("{KDV_TOPLAM}", borcAnalizSonuclari.getKdvTutari() + "")
        );

        dto.add(borcAnalizSonuclari);

        // %8 KDV
        Double yuzdeSekizKdvAlacakToplam = yuzdeSekizKdvAlacakToplamiHesapla(hesapPlaniUploadId, hesapHareketleriUploadId);
        Map<String, Object> yuzdeSekizAlacakIndKDV = indirilecekKDVOranIleGetirAlacakIcin(indirilecekKDVAlacakUploadId, 8L);
        borcAnalizSonuclari = new KdvAnalizCevapDTO();

        borcAnalizSonuclari.setTip(toplamTipi);
        borcAnalizSonuclari.setHesapKodlari(hesapKodlari);
        borcAnalizSonuclari.setBorcTutari(yuzdeSekizKdvAlacakToplam);
        borcAnalizSonuclari.setKdvOrani(8L);
        borcAnalizSonuclari.setFirmaAdi((String) yuzdeSekizAlacakIndKDV.get("FIRMA_ADI"));
        borcAnalizSonuclari.setKdvTutari((Double) yuzdeSekizAlacakIndKDV.get("KDV_TUTARI"));
        borcAnalizSonuclari.setVergiNo((String) yuzdeSekizAlacakIndKDV.get("FIRMA_VERGI_NO"));
        borcAnalizSonuclari.setSonuc(
                (
                        borcAnalizSonuclari.getBorcTutari().doubleValue() != borcAnalizSonuclari.getKdvTutari().doubleValue() ?
                                KdvAnalizCevapDTO.AnalizDurumlari.TUTARLAR_FARKLI : KdvAnalizCevapDTO.AnalizDurumlari.TUTARLAR_ESIT
                )
        );
        borcAnalizSonuclari.setMesaj(
                KdvAnalizCevapDTO.AnalizDurumlari.TUTARLAR_ESIT.equals(borcAnalizSonuclari.getSonuc()) ?
                        positiveMessageTemlate
                                .replace("{FIRMA}", borcAnalizSonuclari.getFirmaAdi())
                                .replace("{KDV_ORAN}", borcAnalizSonuclari.getKdvOrani() + "")
                        :
                        negativeMessageTemplate
                                .replace("{FIRMA}", borcAnalizSonuclari.getFirmaAdi())
                                .replace("{KDV_ORAN}", borcAnalizSonuclari.getKdvOrani() + "")
                                .replace("{TOPLAM}", borcAnalizSonuclari.getBorcTutari() + "")
                                .replace("{KDV_TOPLAM}", borcAnalizSonuclari.getKdvTutari() + "")
        );

        dto.add(borcAnalizSonuclari);

        // %18 KDV
        Double yuzdeOnSekizKdvAlacakToplam = yuzdeOnSekizKdvAlacakToplamiHesapla(hesapPlaniUploadId, hesapHareketleriUploadId);
        Map<String, Object> yuzdeOnSekizAlacakIndKDV = indirilecekKDVOranIleGetirBorcIcin(indirilecekKDVAlacakUploadId, 8L);
        borcAnalizSonuclari = new KdvAnalizCevapDTO();

        borcAnalizSonuclari.setTip(toplamTipi);
        borcAnalizSonuclari.setHesapKodlari(hesapKodlari);
        borcAnalizSonuclari.setBorcTutari(yuzdeOnSekizKdvAlacakToplam);
        borcAnalizSonuclari.setKdvOrani(18L);
        borcAnalizSonuclari.setFirmaAdi((String) yuzdeOnSekizAlacakIndKDV.get("FIRMA_ADI"));
        borcAnalizSonuclari.setKdvTutari((Double) yuzdeOnSekizAlacakIndKDV.get("KDV_TUTARI"));
        borcAnalizSonuclari.setVergiNo((String) yuzdeOnSekizAlacakIndKDV.get("FIRMA_VERGI_NO"));
        borcAnalizSonuclari.setSonuc(
                (
                        borcAnalizSonuclari.getBorcTutari().doubleValue() != borcAnalizSonuclari.getKdvTutari().doubleValue() ?
                                KdvAnalizCevapDTO.AnalizDurumlari.TUTARLAR_FARKLI : KdvAnalizCevapDTO.AnalizDurumlari.TUTARLAR_ESIT
                )
        );
        borcAnalizSonuclari.setMesaj(
                KdvAnalizCevapDTO.AnalizDurumlari.TUTARLAR_ESIT.equals(borcAnalizSonuclari.getSonuc()) ?
                        positiveMessageTemlate
                                .replace("{FIRMA}", borcAnalizSonuclari.getFirmaAdi())
                                .replace("{KDV_ORAN}", borcAnalizSonuclari.getKdvOrani() + "")
                        :
                        negativeMessageTemplate
                                .replace("{FIRMA}", borcAnalizSonuclari.getFirmaAdi())
                                .replace("{KDV_ORAN}", borcAnalizSonuclari.getKdvOrani() + "")
                                .replace("{TOPLAM}", borcAnalizSonuclari.getBorcTutari() + "")
                                .replace("{KDV_TOPLAM}", borcAnalizSonuclari.getKdvTutari() + "")
        );

        dto.add(borcAnalizSonuclari);

        // TOPLAMLAR TEYIDI
        KDVOzel kdvOzel = kdvOzelRepository.findByUploadid(kdvOzelUploadId);
        if (kdvOzel == null) {
            log.warn("kdv ozel bigisi uploadid: " + kdvOzelUploadId + " ile bulunmadi");
        }

        toplamTipi = KdvAnalizCevapDTO.AnalizToplamTipi.KEBIR_KODU_ALACAK;

        // KEBIR 600 601 602 AYLIK
        negativeMessageTemplate = messageSource.getMessage("kdvanaliz.negative.message1", null, locale);
        positiveMessageTemlate = messageSource.getMessage("kdvanaliz.positive.message1", null, locale);
        List<String> kebirKodlari = new LinkedList<>();
        kebirKodlari.add("600");
        kebirKodlari.add("601");
        kebirKodlari.add("602");

        Double kebir600601602Toplam = 0D;
        try {
            kebir600601602Toplam =
                    hesapHareketleriRepository.findSumByKebirkoduAndUploadidAlacak(kebirKodlari, hesapHareketleriUploadId);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        borcAnalizSonuclari = new KdvAnalizCevapDTO();

        borcAnalizSonuclari.setTip(toplamTipi);
        borcAnalizSonuclari.setKebirKodlari(kebirKodlari);
        borcAnalizSonuclari.setBorcTutari(kebir600601602Toplam);
        borcAnalizSonuclari.setKdvTutari(Double.valueOf(kdvOzel.getTeslimvehizmetleriteskiledenbedelaylik()));
        borcAnalizSonuclari.setSonuc(
                (
                        (
                                kdvOzel.getTeslimvehizmetleriteskiledenbedelaylik() != null
                                        && Double.valueOf(kdvOzel.getTeslimvehizmetleriteskiledenbedelaylik()).doubleValue() != kebir600601602Toplam.doubleValue()
                        ) ?
                                KdvAnalizCevapDTO.AnalizDurumlari.TUTARLAR_FARKLI : KdvAnalizCevapDTO.AnalizDurumlari.TUTARLAR_ESIT
                )
        );
        borcAnalizSonuclari.setMesaj(
                KdvAnalizCevapDTO.AnalizDurumlari.TUTARLAR_ESIT.equals(borcAnalizSonuclari.getSonuc()) ?
                        positiveMessageTemlate
                        :
                        negativeMessageTemplate
                                .replace("{TOPLAM}", borcAnalizSonuclari.getBorcTutari() + "")
                                .replace("{KDV_TOPLAM}", borcAnalizSonuclari.getKdvTutari() + "")
        );

        dto.add(borcAnalizSonuclari);

        // KEBIR 600 601 602 KUMULATIF
        negativeMessageTemplate = messageSource.getMessage("kdvanaliz.negative.message2", null, locale);
        positiveMessageTemlate = messageSource.getMessage("kdvanaliz.positive.message2", null, locale);
        kebirKodlari = new LinkedList<>();
        kebirKodlari.add("600");
        kebirKodlari.add("601");
        kebirKodlari.add("602");

        kebir600601602Toplam = 0D;
        try {
            kebir600601602Toplam =
                    hesapHareketleriRepository.findSumByKebirkoduAndUploadidAlacak(kebirKodlari, hesapHareketleriUploadId);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        borcAnalizSonuclari = new KdvAnalizCevapDTO();

        borcAnalizSonuclari.setTip(toplamTipi);
        borcAnalizSonuclari.setKebirKodlari(kebirKodlari);
        borcAnalizSonuclari.setBorcTutari(kebir600601602Toplam);
        borcAnalizSonuclari.setKdvTutari(Double.valueOf(kdvOzel.getTeslimvehizmetleriteskiledenbedelaylik()));
        borcAnalizSonuclari.setSonuc(
                (
                        (
                                kdvOzel.getTeslimvehizmetleriteskiledenbedelkumulatif() != null
                                        && Double.valueOf(kdvOzel.getTeslimvehizmetleriteskiledenbedelkumulatif()).doubleValue() != kebir600601602Toplam.doubleValue()
                        ) ?
                                KdvAnalizCevapDTO.AnalizDurumlari.TUTARLAR_FARKLI : KdvAnalizCevapDTO.AnalizDurumlari.TUTARLAR_ESIT
                )
        );
        borcAnalizSonuclari.setMesaj(
                KdvAnalizCevapDTO.AnalizDurumlari.TUTARLAR_ESIT.equals(borcAnalizSonuclari.getSonuc()) ?
                        positiveMessageTemlate
                        :
                        negativeMessageTemplate
                                .replace("{TOPLAM}", borcAnalizSonuclari.getBorcTutari() + "")
                                .replace("{KDV_TOPLAM}", borcAnalizSonuclari.getKdvTutari() + "")
        );

        dto.add(borcAnalizSonuclari);

        // KEBIR 108
        toplamTipi = KdvAnalizCevapDTO.AnalizToplamTipi.KEBIR_KODU_BORC;
        negativeMessageTemplate = messageSource.getMessage("kdvanaliz.negative.message3", null, locale);
        positiveMessageTemlate = messageSource.getMessage("kdvanaliz.positive.message3", null, locale);
        kebirKodlari = new LinkedList<>();
        kebirKodlari.add("108");

        kebir600601602Toplam = 0D;
        try {
            kebir600601602Toplam =
                    hesapHareketleriRepository.findSumByKebirkoduAndUploadidAndYemiyenoBorc(kebirKodlari, hesapHareketleriUploadId);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        borcAnalizSonuclari = new KdvAnalizCevapDTO();

        borcAnalizSonuclari.setTip(toplamTipi);
        borcAnalizSonuclari.setKebirKodlari(kebirKodlari);
        borcAnalizSonuclari.setBorcTutari(kebir600601602Toplam);
        borcAnalizSonuclari.setKdvTutari(Double.valueOf(kdvOzel.getTeslimvehizmetleriteskiledenbedelaylik()));
        borcAnalizSonuclari.setSonuc(
                (
                        (
                                kdvOzel.getKredikarti() != null
                                        && Double.valueOf(kdvOzel.getKredikarti()).doubleValue() != kebir600601602Toplam.doubleValue()
                        ) ?
                                KdvAnalizCevapDTO.AnalizDurumlari.TUTARLAR_FARKLI : KdvAnalizCevapDTO.AnalizDurumlari.TUTARLAR_ESIT
                )
        );
        borcAnalizSonuclari.setMesaj(
                KdvAnalizCevapDTO.AnalizDurumlari.TUTARLAR_ESIT.equals(borcAnalizSonuclari.getSonuc()) ?
                        positiveMessageTemlate
                        :
                        negativeMessageTemplate
                                .replace("{TOPLAM}", borcAnalizSonuclari.getBorcTutari() + "")
                                .replace("{KDV_TOPLAM}", borcAnalizSonuclari.getKdvTutari() + "")
        );

        dto.add(borcAnalizSonuclari);

        // KEBIR 190
        negativeMessageTemplate = messageSource.getMessage("kdvanaliz.negative.message4", null, locale);
        positiveMessageTemlate = messageSource.getMessage("kdvanaliz.positive.message4", null, locale);
        toplamTipi = KdvAnalizCevapDTO.AnalizToplamTipi.KEBIR_KODU_BORC;
        Double kdvBorcToplam = yuzdeBirKdvBorcToplam + yuzdeOnSekizKdvBorcToplam + yuzdeOnSekizKdvBorcToplam;
        Double kdvAlacakToplam = yuzdeBirKdvAlacakToplam + yuzdeSekizKdvAlacakToplam + yuzdeOnSekizKdvAlacakToplam;
        Double kdvBorcAlacakFark = kdvBorcToplam - kdvAlacakToplam;
        kebirKodlari = new LinkedList<>();
        kebirKodlari.add("190");
        Double kdvDevreden = hesapHareketleriRepository.findSumByKebirkoduAndUploadidAndYemiyenoBorc(kebirKodlari, hesapHareketleriUploadId);

        if (kdvDevreden == null) {
            log.warn("KDV devreden NULL kebir kodu 190");
        }

        if (kdvDevreden != null && kdvBorcAlacakFark.doubleValue() != kdvDevreden.doubleValue()) {
            borcAnalizSonuclari = new KdvAnalizCevapDTO();

            borcAnalizSonuclari.setTip(toplamTipi);
            borcAnalizSonuclari.setKebirKodlari(kebirKodlari);
            borcAnalizSonuclari.setBorcTutari(kdvBorcAlacakFark);
            borcAnalizSonuclari.setKdvTutari(kdvDevreden);
            borcAnalizSonuclari.setSonuc(
                    (
                            (
                                    borcAnalizSonuclari.getBorcTutari().doubleValue() != borcAnalizSonuclari.getKdvTutari().doubleValue()
                            ) ?
                                    KdvAnalizCevapDTO.AnalizDurumlari.TUTARLAR_FARKLI : KdvAnalizCevapDTO.AnalizDurumlari.TUTARLAR_ESIT
                    )
            );
            borcAnalizSonuclari.setMesaj(
                    KdvAnalizCevapDTO.AnalizDurumlari.TUTARLAR_ESIT.equals(borcAnalizSonuclari.getSonuc()) ?
                            positiveMessageTemlate
                            :
                            negativeMessageTemplate
                                    .replace("{TOPLAM}", borcAnalizSonuclari.getBorcTutari() + "")
                                    .replace("{KDV_TOPLAM}", borcAnalizSonuclari.getKdvTutari() + "")
            );

            dto.add(borcAnalizSonuclari);
        } else {

            negativeMessageTemplate = messageSource.getMessage("kdvanaliz.negative.message5", null, locale);
            positiveMessageTemlate = messageSource.getMessage("kdvanaliz.positive.message5", null, locale);
            toplamTipi = KdvAnalizCevapDTO.AnalizToplamTipi.KEBIR_KODU_ALACAK;
            kdvBorcAlacakFark = kdvAlacakToplam - kdvBorcToplam;
            Double bh = kdvAlacakToplamiHesapla2(hesapPlaniUploadId, hesapHareketleriUploadId);
            if (bh == null) {
                log.warn("kdvAlacakToplamiHesapla2 NULL!");
            }


            borcAnalizSonuclari = new KdvAnalizCevapDTO();

            borcAnalizSonuclari.setTip(toplamTipi);
            borcAnalizSonuclari.setKebirKodlari(kebirKodlari);
            borcAnalizSonuclari.setBorcTutari(kdvBorcAlacakFark);
            borcAnalizSonuclari.setKdvTutari(bh);
            borcAnalizSonuclari.setSonuc(
                    (
                            (
                                    borcAnalizSonuclari.getBorcTutari().doubleValue() != borcAnalizSonuclari.getKdvTutari().doubleValue()
                            ) ?
                                    KdvAnalizCevapDTO.AnalizDurumlari.TUTARLAR_FARKLI : KdvAnalizCevapDTO.AnalizDurumlari.TUTARLAR_ESIT
                    )
            );
            borcAnalizSonuclari.setMesaj(
                    KdvAnalizCevapDTO.AnalizDurumlari.TUTARLAR_ESIT.equals(borcAnalizSonuclari.getSonuc()) ?
                            positiveMessageTemlate
                            :
                            negativeMessageTemplate
                                    .replace("{TOPLAM}", borcAnalizSonuclari.getBorcTutari() + "")
                                    .replace("{KDV_TOPLAM}", borcAnalizSonuclari.getKdvTutari() + "")
            );

            dto.add(borcAnalizSonuclari);

        }

        return dto;
    }

    /**
     * KDV BORC Toplamini getirir.
     *
     * @param hesapPlaniUploadId
     * @param hesapHareketleriUploadId
     * @return
     */
    public Double kdvBorcToplamiHesapla(Long hesapPlaniUploadId, Long hesapHareketleriUploadId, String yuzdeString) throws KdvAnalizException {

        String yuzdeNotLike = "%18";
        if (yuzdeString.equals("%18")) {
            yuzdeNotLike = "";
        }
        Double result = 0D;

        try {

            List<String> hesapKodlari = hesapPlaniRepository.kdvIcinHesapPlaniGetirUploadIdIle("191", "192", yuzdeString, "%18", hesapPlaniUploadId);
            if (hesapKodlari == null || hesapKodlari.size() == 0) {
                log.warn("*** KdvAnalizService#kdvBorcToplamiHesapla, upload id: "
                        + hesapPlaniUploadId + " icin herhangi bir hesap kodu bulunamadi!");
                return result;
            }
            for (String hesapKodu : hesapKodlari) {
                Double borclar = null;

                try {

                    borclar = hesapHareketleriRepository.muhasebehesapkoduVeUploadidIleBorclarToplaminiGetir(
                            hesapKodu.trim().replace(".", ""),
                            hesapHareketleriUploadId
                    );

                } catch (Exception e) {
                    log.error(e.getMessage());
                }

                if (borclar == null) {
                    log.warn("*** KdvAnalizService#kdvBorcToplamiHesapla, upload id: "
                            + hesapHareketleriUploadId + " - hesap kodu:"
                            + hesapKodu + " borc toplami bulunamadi!");

                    continue;
                }

                result += borclar;

            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new KdvAnalizException(e.getMessage());
        }

        return result;
    }

    /**
     * KDV alacak toplamini getirir.
     *
     * @param hesapPlaniUploadId
     * @param hesapHareketleriUploadId
     * @param yuzdeString
     * @return
     * @throws KdvAnalizException
     */
    public Double kdvAlacakToplamiHesapla(Long hesapPlaniUploadId, Long hesapHareketleriUploadId, String yuzdeString) throws KdvAnalizException {

        String yuzdeNotLike = "%18";
        if (yuzdeString.equals("%18")) {
            yuzdeNotLike = "";
        }
        Double result = 0D;

        try {

            List<String> hesapKodlari = hesapPlaniRepository.kdvIcinHesapPlaniGetirUploadIdIle("391", "", yuzdeString, "%18", hesapPlaniUploadId);
            if (hesapKodlari == null || hesapKodlari.size() == 0) {
                log.warn("*** KdvAnalizService#kdvAlacakToplamiHesapla, upload id: "
                        + hesapPlaniUploadId + " icin herhangi bir hesap kodu bulunamadi!");
                return result;
            }
            for (String hesapKodu : hesapKodlari) {
                Double borclar = null;

                try {

                    borclar = hesapHareketleriRepository.muhasebehesapkoduVeUploadidIleAlacaklarToplaminiGetir(
                            hesapKodu.trim().replace(".", ""),
                            hesapHareketleriUploadId
                    );

                } catch (Exception e) {
                    log.error(e.getMessage());
                }

                if (borclar == null) {
                    log.warn("*** KdvAnalizService#kdvAlacakToplamiHesapla, upload id: "
                            + hesapHareketleriUploadId + " - hesap kodu:"
                            + hesapKodu + " alacak toplami bulunamadi!");
                    continue;
                }

                result += borclar;

            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new KdvAnalizException(e.getMessage());
        }

        return result;
    }

    /**
     * @param indirilecekKDVUploadId
     * @param oran
     * @return
     */
    public Map<String, Object> indirilecekKDVOranIleGetirAlacakIcin(Long indirilecekKDVUploadId, Long oran) {
        Map<String, Object> result = null;
        List<FirmaGenel> firmaGenels = firmaGenelRepository.findAllDistinctByVergino();
        if (firmaGenels == null || firmaGenels.size() == 0) {
            log.warn("Indirilecek KDV icin firma bulunamadi! Firma genel BOS!"
                    + " upload id: " + indirilecekKDVUploadId
                    + " - KDV oran : " + oran);
            return result;
        }

        TevkifatTablosu indirilecekKDVForBAGenel = tevkifatTablosuRepository.findTop1ByUploadid(indirilecekKDVUploadId);
        BAGenel baGenel = baGenelRepository.findOne(indirilecekKDVForBAGenel.getBagenelid());
        TevkifatTablosu indirilecekKDV = tevkifatTablosuRepository.findTop1ByUploadidAndOranAndBagenelid(indirilecekKDVUploadId, oran, indirilecekKDVForBAGenel.getBagenelid());
        if (indirilecekKDV == null) {
            log.warn("Vergi No: " + baGenel.getMukellef() + " - icin indirilecek KDV bulunamadi.");
            return result;
        }

        FirmaGenel firmaGenel = firmaGenelRepository.findTop1ByVergino(baGenel.getMukellef());
        result = new LinkedHashMap<>();

        result.put("FIRMA_ADI", firmaGenel.getAdi());
        result.put("FIRMA_VERGI_NO", firmaGenel.getVergino());
        result.put("KDV_BEDELI", Double.valueOf(indirilecekKDV.getMatrah()));
        result.put("KDV_TUTARI", Double.valueOf(indirilecekKDV.getVergi()));
        result.put("KDV_ORANI", Long.valueOf(indirilecekKDV.getOran()));

        return result;
    }

    /**
     * @param indirilecekKDVUploadId
     * @param oran
     * @return
     */
    public Map<String, Object> indirilecekKDVOranIleGetirBorcIcin(Long indirilecekKDVUploadId, Long oran) {
        Map<String, Object> result = null;
        List<FirmaGenel> firmaGenels = firmaGenelRepository.findAllDistinctByVergino();
        if (firmaGenels == null || firmaGenels.size() == 0) {
            log.warn("Indirilecek KDV icin firma bulunamadi! Firma genel BOS!"
                    + " upload id: " + indirilecekKDVUploadId
                    + " - KDV oran : " + oran);
            return result;
        }

        IndirilecekKDV indirilecekKDVForBAGenel = indirilecekKDVRepository.findTop1ByUploadid(indirilecekKDVUploadId);
        BAGenel baGenel = baGenelRepository.findOne(indirilecekKDVForBAGenel.getBagenelid());
        IndirilecekKDV indirilecekKDV = indirilecekKDVRepository.findTop1ByUploadidAndOranAndBagenelid(indirilecekKDVUploadId, oran, indirilecekKDVForBAGenel.getBagenelid());
        if (indirilecekKDV == null) {
            log.warn("Vergi No: " + baGenel.getMukellef() + " - icin indirilecek KDV bulunamadi.");
            return result;
        }

        FirmaGenel firmaGenel = firmaGenelRepository.findTop1ByVergino(baGenel.getMukellef());
        result = new LinkedHashMap<>();

        result.put("FIRMA_ADI", firmaGenel.getAdi());
        result.put("FIRMA_VERGI_NO", firmaGenel.getVergino());
        result.put("KDV_BEDELI", Double.valueOf(indirilecekKDV.getBedel()));
        result.put("KDV_TUTARI", Double.valueOf(indirilecekKDV.getKdvtutari()));
        result.put("KDV_ORANI", Long.valueOf(indirilecekKDV.getOran()));

        return result;
    }

    /**
     * @param hesapPlaniUploadId
     * @param hesapHareketleriUploadId
     * @return
     * @throws KdvAnalizException
     */
    public Double kdvAlacakToplamiHesapla2(Long hesapPlaniUploadId, Long hesapHareketleriUploadId) throws KdvAnalizException {

        Double result = 0D;

        try {

            List<String> hesapKodlari = hesapPlaniRepository.kdvIcinHesapPlaniGetirUploadIdIle2("360", "ÖDENECEK KDV1", "1 NOLU KDV ÖDEME", hesapPlaniUploadId);
            if (hesapKodlari == null || hesapKodlari.size() == 0) {
                log.warn("*** KdvAnalizService#kdvAlacakToplamiHesapla, upload id: "
                        + hesapPlaniUploadId + " icin herhangi bir hesap kodu bulunamadi!");
                return result;
            }
            for (String hesapKodu : hesapKodlari) {
                Double borclar = null;

                try {

                    borclar = hesapHareketleriRepository.muhasebehesapkoduVeUploadidIleAlacaklarToplaminiGetir(
                            hesapKodu.trim().replace(".", ""),
                            hesapHareketleriUploadId
                    );

                } catch (Exception e) {
                    log.error(e.getMessage());
                }

                if (borclar == null) {
                    log.warn("*** KdvAnalizService#kdvAlacakToplamiHesapla2, upload id: "
                            + hesapHareketleriUploadId + " - hesap kodu:"
                            + hesapKodu + " alacak toplami bulunamadi!");
                    continue;
                }

                result += borclar;

            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new KdvAnalizException(e.getMessage());
        }

        return result;
    }

}