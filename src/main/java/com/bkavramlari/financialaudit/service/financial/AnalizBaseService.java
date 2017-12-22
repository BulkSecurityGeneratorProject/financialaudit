package com.bkavramlari.financialaudit.service.financial;

import com.bkavramlari.financialaudit.domain.enums.UploadFileTypes;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * KDV analizi islemleri - "islem5.php"
 * <p>
 * Created by yildizib on 06/11/2017.
 */
@Data
@Slf4j
@Service
public class AnalizBaseService {

    @Autowired
    protected UploadService uploadService;

    /**
     * Tablolara gore yuklenme id leri doner
     */
    protected Map<Tables, Long> getUploadIdsByTableName() {
        log.info(" *** AnalizBaseService getUploadIdsByTableName START *** ");
        Map<UploadFileTypes, Long> uploadIdMap = new LinkedHashMap<>();
        Map<Tables, Long> uploadIdByTableMap = new LinkedHashMap<>();

        for (UploadFileTypes fileType : UploadFileTypes.values()) {
            Long uploadId = uploadService.getLastUploadByCurrentUser(fileType);
            if (uploadId != null) {

                uploadIdMap.put(fileType, uploadId);
                switch (fileType) {
                    case HESAP_PLANI:
                        uploadIdByTableMap.put(Tables.HESAPPLANI, uploadId);
                        break;
                    case HESAP_HAREKETLERI:
                        uploadIdByTableMap.put(Tables.HESAPHAREKETLERI, uploadId);
                        uploadIdByTableMap.put(Tables.BUFFER, uploadId);
                        break;
                    case ALIM_FORMU:
                        uploadIdByTableMap.put(Tables.BAGENEL, uploadId);
                        uploadIdByTableMap.put(Tables.BAOZEL, uploadId);
                        uploadIdByTableMap.put(Tables.FIRMAGENEL, uploadId);
                        break;
                    case SATIS_FORMU:
                        uploadIdByTableMap.put(Tables.BSOZEL, uploadId);
                        break;
                    case KDV:
                        uploadIdByTableMap.put(Tables.KDVOZEL, uploadId);
                        uploadIdByTableMap.put(Tables.TEVKIFATTABLOSU, uploadId);
                        uploadIdByTableMap.put(Tables.INDIRIMLER, uploadId);
                        uploadIdByTableMap.put(Tables.INDIRILECEKKDV, uploadId);
                        break;
                    case KITAP:
                        uploadIdByTableMap.put(Tables.KITAP, uploadId);
                        break;
                }
            }
        }
        log.info(" *** AnalizBaseService getUploadIdsByTableName END *** ");

        return uploadIdByTableMap;
    }

    /**
     * Dosya tipine gore yukleme id doner.
     *
     * @return
     */
    protected Map<UploadFileTypes, Long> getUploadIdsByFileType() {
        log.info(" *** AnalizBaseService getUploadIdsByFileType START *** ");
        Map<UploadFileTypes, Long> uploadIdMap = new LinkedHashMap<>();

        for (UploadFileTypes fileType : UploadFileTypes.values()) {
            Long uploadId = uploadService.getLastUploadByCurrentUser(fileType);
            if (uploadId != null) {
                uploadIdMap.put(fileType, uploadId);
            }
        }
        log.info(" *** AnalizBaseService getUploadIdsByFileType END *** ");

        return uploadIdMap;
    }

    /**
     * Tablolar
     */
    protected enum Tables {
        BAGENEL,
        BAOZEL,
        BSOZEL,
        BUFFER,
        FIRMAGENEL,
        HESAPHAREKETLERI,
        HESAPPLANI,
        INDIRILECEKKDV,
        INDIRIMLER,
        KDVOZEL,
        KITAP,
        TEVKIFATTABLOSU,
        UPLOAD
    }
}
