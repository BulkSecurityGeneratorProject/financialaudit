package com.bkavramlari.financialaudit.service.financial;

import com.bkavramlari.financialaudit.config.CacheConfig;
import com.bkavramlari.financialaudit.domain.dto.BsAnalizCevapDTO;
import com.bkavramlari.financialaudit.domain.financial.BSOzel;
import com.bkavramlari.financialaudit.domain.financial.BsAnalizView;
import com.bkavramlari.financialaudit.repository.financial.BSOzelRepository;
import com.bkavramlari.financialaudit.repository.financial.BsAnalizViewRepository;
import com.bkavramlari.financialaudit.service.UserService;
import com.bkavramlari.financialaudit.service.financial.exceptions.BsAnalizException;
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
public class BsAnalizService extends AnalizBaseService {

    @Autowired
    private BSOzelRepository bsOzelRepository;
    @Autowired
    private BsAnalizViewRepository bsAnalizViewRepository;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private UserService userService;

    @Cacheable(cacheNames = CacheConfig.ICacheNames.DAILY)
    public List<BsAnalizCevapDTO> analizEt(String loginName, Long lastUploadByCurrentUser, Pageable pageable) throws BsAnalizException {

        List<BsAnalizCevapDTO> dto = new LinkedList<>();
        Map<Tables, Long> uploadIdsByTableName = getUploadIdsByTableName();
        Long kitapUploadId = uploadIdsByTableName.get(Tables.KITAP);
        Long bufferUploadId = uploadIdsByTableName.get(Tables.BUFFER);
        Long hesapHareketleriUploadId = uploadIdsByTableName.get(Tables.HESAPHAREKETLERI);
        Long bsOzelUploadId = uploadIdsByTableName.get(Tables.BSOZEL);
        Locale locale = Locale.forLanguageTag(userService.getUserWithAuthorities().get().getLangKey());
        String negativeMessageTemplate = messageSource.getMessage("bsanaliz.negative.message", null, locale);
        String positiveMessageTemlate = messageSource.getMessage("bsanaliz.positive.message", null, locale);
        String noneMessageTemlate = messageSource.getMessage("bsanaliz.none.message", null, locale);

        List<BsAnalizView> bsAnalizViews = bsAnalizViewRepository.findByUploadIds(hesapHareketleriUploadId, bufferUploadId, kitapUploadId);
        if (bsAnalizViews == null || bsAnalizViews.size() == 0) {
            throw new BsAnalizException("BsAnalizService donen liste NULL");
        }

        int satirSayisi = 1;
        for (BsAnalizView baw : bsAnalizViews) {
            BsAnalizCevapDTO bawDTO = new BsAnalizCevapDTO();

            bawDTO.setBsViewSatirNo(satirSayisi++);
            bawDTO.setBsViewToplamBelgeSayisi(baw.getCAlacak());
            bawDTO.setBsViewToplamAlacak(baw.getAlacak());
            bawDTO.setBsViewToplamBorc(baw.getBorc());
            bawDTO.setBsViewVergiNo(baw.getVergiNo());

            bawDTO.setBsViewToplamAlacakveBorcFarki(bawDTO.getBsViewToplamAlacak() - bawDTO.getBsViewToplamBorc());

            if (bawDTO.getBsViewToplamAlacakveBorcFarki() > 5000) {

                BSOzel bsOzel = bsOzelRepository.findByVkNoAndUploadid(bawDTO.getBsViewVergiNo(), bsOzelUploadId);
                if (bsOzel == null) {

                    bawDTO.setBsViewSonuc(BsAnalizCevapDTO.AnalizDurumlari.BELGE_SAYILARI_YOK);
                    bawDTO.setBsViewMesaj(
                            noneMessageTemlate.replace("{VERGI_NO}", bawDTO.getBsViewVergiNo())
                                    .replace("{BSW_BELGE_SAYISI}", bawDTO.getBsViewToplamBelgeSayisi() + "")
                                    .replace("{BSW_GENEL_TOPLAM}", bawDTO.getBsViewToplamAlacakveBorcFarki() + "")
                    );

                    dto.add(bawDTO);
                    continue;
                }

                bawDTO.setBsOzelBelgeSayisi(bsOzel.getBelgesayisi());
                bawDTO.setBsOzelMalHizmetBedeli(bsOzel.getMalhizmetbedeli());
                bawDTO.setBsViewUnvan(bsOzel.getUnvan());
                if (bawDTO.getBsViewToplamBelgeSayisi() == bawDTO.getBsOzelBelgeSayisi()) {

                    bawDTO.setBsViewSonuc(BsAnalizCevapDTO.AnalizDurumlari.BELGE_SAYILARI_ESIT);
                    bawDTO.setBsViewMesaj(
                            positiveMessageTemlate.replace("{UNVAN}", bawDTO.getBsViewUnvan())
                    );

                } else {

                    bawDTO.setBsViewSonuc(BsAnalizCevapDTO.AnalizDurumlari.BELGE_SAYILARI_FARKLI);
                    bawDTO.setBsViewMesaj(
                            negativeMessageTemplate.replace("{UNVAN}", bawDTO.getBsViewUnvan())
                                    .replace("{VERGI_NO}", bawDTO.getBsViewVergiNo())
                                    .replace("{BSW_BELGE_SAYISI}", bawDTO.getBsViewToplamBelgeSayisi() + "")
                                    .replace("{BSW_GENEL_TOPLAM}", bawDTO.getBsViewToplamAlacakveBorcFarki() + "")
                                    .replace("{BSOZEL_BELGE_SAYISI}", bawDTO.getBsOzelBelgeSayisi() + "")
                                    .replace("{BSOZEL_GENEL_TOPLAM}", bawDTO.getBsOzelMalHizmetBedeli() + "")
                    );

                }

                dto.add(bawDTO);
            }
        }

        return dto;
    }

}