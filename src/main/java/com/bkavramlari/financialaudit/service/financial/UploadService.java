package com.bkavramlari.financialaudit.service.financial;

import com.bkavramlari.financialaudit.domain.enums.UploadFileProcStats;
import com.bkavramlari.financialaudit.domain.enums.UploadFileTypes;
import com.bkavramlari.financialaudit.domain.financial.Upload;
import com.bkavramlari.financialaudit.domain.security.User;
import com.bkavramlari.financialaudit.repository.financial.UploadRepository;
import com.bkavramlari.financialaudit.service.UserService;
import com.bkavramlari.financialaudit.service.financial.parser.csv.exceptions.CsvParseServiceException;
import com.bkavramlari.financialaudit.service.financial.parser.xml.exceptions.XmlParseServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Service class for managing users.
 */
@Service
public class UploadService {

    private final Logger log = LoggerFactory.getLogger(UploadService.class);
    @Autowired
    private UploadRepository uploadRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private HesapPlaniService hesapPlaniService;
    @Autowired
    private HesapHareketleriService hesapHareketleriService;
    @Autowired
    private AlimFormuService alimFormuService;
    @Autowired
    private SatisFormuService satisFormuService;
    @Autowired
    private KdvFormuService kdvFormuService;
    @Autowired
    private KitapService kitapService;

    /**
     * @param fileName
     * @param extension
     * @param filePublicPath
     * @param originalName
     * @param fileType
     * @param status
     * @return
     */
    @Transactional
    public Upload save(String fileName, String extension, String filePublicPath, String originalName, UploadFileTypes fileType, UploadFileProcStats status) {

        Upload result = null;
        try {
            result = new Upload();

            result.setDosyaadi(fileName);
            result.setDosyayolu(filePublicPath);
            result.setDosyauzantisi(extension);
            result.setOriginalFileName(originalName);
            result.setFileType(fileType);
            result.setStatus(status);

            if (userService.getUserWithAuthorities().isPresent()) {
                result.setUserId(userService.getUserWithAuthorities().get().getId());
            }

            uploadRepository.save(result);

        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return result;
    }

    /**
     * @param upload
     * @return
     */
    @Transactional
    public Upload save(Upload upload) {
        return uploadRepository.save(upload);
    }

    /**
     *
     */
    @Transactional
    public void parseProcess() throws CsvParseServiceException, XmlParseServiceException {
        User user = userService.getUserWithAuthorities().get();
        List<Upload> uploads = uploadRepository.findByStatusAndUserIdOrderByIdAsc(UploadFileProcStats.NEW, user.getId());
        if (uploads == null || uploads.size() == 0) {
            log.warn("*** DATE " + new Date() + " - NEW statuslu upload edilmis dosya bulunamadi! ***");
            return;
        }
        for (Upload upload : uploads) {
            Upload u = uploadRepository.findOne(upload.getId());
            u.setStatus(UploadFileProcStats.PROCESSING);
            save(u);
        }
        for (Upload upload : uploads) {

            Upload u = uploadRepository.findOne(upload.getId());
            u.setStartDate(new Date());
            save(u);

            switch (upload.getFileType()) {
                case HESAP_HAREKETLERI:
                    hesapHareketleriService.parse(upload.getDosyayolu(), upload.getDosyaadi(), upload.getId());
                    break;
                case HESAP_PLANI:
                    hesapPlaniService.parse(upload.getDosyayolu(), upload.getDosyaadi(), upload.getId());
                    break;
                case SATIS_FORMU:
                    satisFormuService.parse(upload.getDosyayolu(), upload.getDosyaadi(), upload.getId());
                    break;
                case ALIM_FORMU:
                    alimFormuService.parse(upload.getDosyayolu(), upload.getDosyaadi(), upload.getId());
                    break;
                case KITAP:
                    kitapService.parse(upload.getDosyayolu(), upload.getDosyaadi(), upload.getId());
                    break;
                case KDV:
                    kdvFormuService.parse(upload.getDosyayolu(), upload.getDosyaadi(), upload.getId());
                    break;
                default:
                    log.warn("*** Yuklenen bir dosya hic bir tipe uygun degil! " + upload.toString());
                    break;
            }

            u = uploadRepository.findOne(upload.getId());
            u.setEndDate(new Date());
            u.setStatus(UploadFileProcStats.COMPLETED);

            save(u);
        }
    }

    /**
     * Dosya tipine gore user'in yukleme ID sini doner.
     *
     * @param fileType
     * @return
     */
    public Long getLastUploadByCurrentUser(UploadFileTypes fileType) {
        List<Upload> uploads = null;
        User user = userService.getUserWithAuthorities().get();
        return uploadRepository.getLastUploadByCurrentUser(user.getId(), fileType);
    }
}
