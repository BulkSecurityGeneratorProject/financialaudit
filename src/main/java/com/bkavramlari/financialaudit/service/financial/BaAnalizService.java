package com.bkavramlari.financialaudit.service.financial;

import com.bkavramlari.financialaudit.config.CacheConfig;
import com.bkavramlari.financialaudit.domain.dto.BaAnalizCevapDTO;
import com.bkavramlari.financialaudit.domain.financial.BAOzel;
import com.bkavramlari.financialaudit.domain.financial.BaAnalizView;
import com.bkavramlari.financialaudit.repository.financial.BAOzelRepository;
import com.bkavramlari.financialaudit.repository.financial.BaAnalizViewRepository;
import com.bkavramlari.financialaudit.service.UserService;
import com.bkavramlari.financialaudit.service.financial.exceptions.BaAnalizException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * BS analizi islemleri - "islem4.php"
 * <p>
 * Created by yildizib on 06/11/2017.
 */
@Data
@Slf4j
@Service
public class BaAnalizService extends AnalizBaseService {

    @Autowired
    private BAOzelRepository baOzelRepository;
    @Autowired
    private BaAnalizViewRepository baAnalizViewRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private UserService userService;

    @Cacheable(cacheNames = CacheConfig.ICacheNames.DAILY)
    public List<BaAnalizCevapDTO> analizEt(String loginName, Long lastUploadByCurrentUser, Pageable pageable) throws BaAnalizException {

        List<BaAnalizCevapDTO> dto = new LinkedList<>();
        Map<Tables, Long> uploadIdsByTableName = getUploadIdsByTableName();
        Long kitapUploadId = uploadIdsByTableName.get(Tables.KITAP);
        Long bufferUploadId = uploadIdsByTableName.get(Tables.BUFFER);
        Long hesapHareketleriUploadId = uploadIdsByTableName.get(Tables.HESAPHAREKETLERI);
        Long baOzelUploadId = uploadIdsByTableName.get(Tables.BAOZEL);
        Locale locale = Locale.forLanguageTag(userService.getUserWithAuthorities().get().getLangKey());
        String negativeMessageTemplate = messageSource.getMessage("baanaliz.negative.message", null, locale);
        String positiveMessageTemlate = messageSource.getMessage("baanaliz.positive.message", null, locale);
        String noneMessageTemlate = messageSource.getMessage("baanaliz.none.message", null, locale);

        List<BaAnalizView> bsAnalizViews = baAnalizViewRepository.findByUploadIds(hesapHareketleriUploadId, bufferUploadId, kitapUploadId);
        if (bsAnalizViews == null || bsAnalizViews.size() == 0) {
            throw new BaAnalizException("BsAnalizService donen liste NULL");
        }

        int satirSayisi = 1;
        for (BaAnalizView baw : bsAnalizViews) {
            BaAnalizCevapDTO bawDTO = new BaAnalizCevapDTO();

            bawDTO.setBaViewSatirNo(satirSayisi++);
            bawDTO.setBaViewToplamBelgeSayisi(baw.getCAlacak());
            bawDTO.setBaViewToplamAlacak(baw.getAlacak());
            bawDTO.setBaViewToplamBorc(baw.getBorc());
            bawDTO.setBaViewVergiNo(baw.getVergiNo());

            bawDTO.setBaViewToplamAlacakveBorcFarki(bawDTO.getBaViewToplamAlacak() - bawDTO.getBaViewToplamBorc());

            if (bawDTO.getBaViewToplamAlacakveBorcFarki() > 5000) {

                BAOzel baOzel = baOzelRepository.findByVkNoAndUploadid(bawDTO.getBaViewVergiNo(), baOzelUploadId);
                if (baOzel == null) {

                    bawDTO.setBaViewSonuc(BaAnalizCevapDTO.AnalizDurumlari.BELGE_SAYILARI_YOK);
                    bawDTO.setBaViewMesaj(
                            noneMessageTemlate.replace("{VERGI_NO}", bawDTO.getBaViewVergiNo())
                                    .replace("{BSW_BELGE_SAYISI}", bawDTO.getBaViewToplamBelgeSayisi() + "")
                                    .replace("{BSW_GENEL_TOPLAM}", bawDTO.getBaViewToplamAlacakveBorcFarki() + "")
                    );

                    dto.add(bawDTO);
                    continue;
                }

                bawDTO.setBaOzelBelgeSayisi(baOzel.getBelgesayisi());
                bawDTO.setBaOzelMalHizmetBedeli(baOzel.getMalhizmetbedeli());
                bawDTO.setBaViewUnvan(baOzel.getUnvan());
                if (bawDTO.getBaViewToplamBelgeSayisi() == bawDTO.getBaOzelBelgeSayisi()) {

                    bawDTO.setBaViewSonuc(BaAnalizCevapDTO.AnalizDurumlari.BELGE_SAYILARI_ESIT);
                    bawDTO.setBaViewMesaj(
                            positiveMessageTemlate.replace("{UNVAN}", bawDTO.getBaViewUnvan())
                    );

                } else {

                    bawDTO.setBaViewSonuc(BaAnalizCevapDTO.AnalizDurumlari.BELGE_SAYILARI_FARKLI);
                    bawDTO.setBaViewMesaj(
                            negativeMessageTemplate.replace("{UNVAN}", bawDTO.getBaViewUnvan())
                                    .replace("{VERGI_NO}", bawDTO.getBaViewVergiNo())
                                    .replace("{BSW_BELGE_SAYISI}", bawDTO.getBaViewToplamBelgeSayisi() + "")
                                    .replace("{BSW_GENEL_TOPLAM}", bawDTO.getBaViewToplamAlacakveBorcFarki() + "")
                                    .replace("{BSOZEL_BELGE_SAYISI}", bawDTO.getBaOzelBelgeSayisi() + "")
                                    .replace("{BSOZEL_GENEL_TOPLAM}", bawDTO.getBaOzelMalHizmetBedeli() + "")
                    );

                }

                dto.add(bawDTO);
            }
        }

        return dto;
    }

}