package com.bkavramlari.financialaudit.resource.financial;

import com.bkavramlari.financialaudit.config.CacheConfig;
import com.bkavramlari.financialaudit.domain.dto.BaAnalizCevapDTO;
import com.bkavramlari.financialaudit.domain.dto.BsAnalizCevapDTO;
import com.bkavramlari.financialaudit.domain.dto.ErrorDTO;
import com.bkavramlari.financialaudit.domain.dto.KdvAnalizCevapDTO;
import com.bkavramlari.financialaudit.domain.enums.UploadFileTypes;
import com.bkavramlari.financialaudit.security.AuthoritiesConstants;
import com.bkavramlari.financialaudit.security.SecurityUtils;
import com.bkavramlari.financialaudit.service.financial.BaAnalizService;
import com.bkavramlari.financialaudit.service.financial.BsAnalizService;
import com.bkavramlari.financialaudit.service.financial.KdvAnalizService;
import com.bkavramlari.financialaudit.service.financial.UploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by benan on 10/1/2015.
 */
@Slf4j
@Controller
@RequestMapping("/api/analiz")
public class AnalizResource {

    @Autowired
    private KdvAnalizService kdvAnalizService;
    @Autowired
    private BsAnalizService bsAnalizService;
    @Autowired
    private BaAnalizService baAnalizService;
    @Autowired
    private UploadService uploadService;
    @Autowired
    private CacheManager cacheManager;



    /**
     * @return
     */
    @RequestMapping(value = "/kdv", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({AuthoritiesConstants.USER, AuthoritiesConstants.ADMIN})
    public ResponseEntity kdvAnaliz(Pageable pageable) {

        ErrorDTO errorDTO = new ErrorDTO();
        List<KdvAnalizCevapDTO> dtos = new LinkedList<>();
        HttpStatus status = HttpStatus.OK;
        Page<KdvAnalizCevapDTO> page = null;
        try {
            dtos = kdvAnalizService.analizEt(SecurityUtils.getCurrentLogin(),
                    uploadService.getLastUploadByCurrentUser(UploadFileTypes.KDV), pageable);
            page = getPage(dtos, pageable);
        } catch (Exception e) {
            log.error(e.getMessage());

            errorDTO.setError(e.getMessage());
            errorDTO.setError_description(e.getLocalizedMessage());

            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDTO);
        }

        return ResponseEntity.status(HttpStatus.OK).body(page);
    }

    /**
     *
     * @return
     */
    @RequestMapping(value = "/bs", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({AuthoritiesConstants.USER, AuthoritiesConstants.ADMIN})
    public ResponseEntity bsAnaliz(Pageable pageable) {

        ErrorDTO errorDTO = new ErrorDTO();
        List<BsAnalizCevapDTO> dtos = new LinkedList<>();
        HttpStatus status = HttpStatus.OK;
        Page<BsAnalizCevapDTO> page = null;
        try {

            dtos = bsAnalizService.analizEt(SecurityUtils.getCurrentLogin(),
                    uploadService.getLastUploadByCurrentUser(UploadFileTypes.SATIS_FORMU), pageable);
            page = getPage(dtos, pageable);
        } catch (Exception e) {
            log.error(e.getMessage());

            errorDTO.setError(e.getMessage());
            errorDTO.setError_description(e.getLocalizedMessage());

            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDTO);
        }

        return ResponseEntity.status(HttpStatus.OK).body(page);
    }


    @RequestMapping(value = "/ba", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({AuthoritiesConstants.USER, AuthoritiesConstants.ADMIN})
    public ResponseEntity baAnaliz(Pageable pageable) {

        ErrorDTO errorDTO = new ErrorDTO();
        List<BaAnalizCevapDTO> dtos = new LinkedList<>();
        HttpStatus status = HttpStatus.OK;
        Page<BaAnalizCevapDTO> page = null;
        try {

            dtos = baAnalizService.analizEt(SecurityUtils.getCurrentLogin(),
                    uploadService.getLastUploadByCurrentUser(UploadFileTypes.ALIM_FORMU), pageable);
            page = getPage(dtos, pageable);
        } catch (Exception e) {
            log.error(e.getMessage());

            errorDTO.setError(e.getMessage());
            errorDTO.setError_description(e.getLocalizedMessage());

            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDTO);
        }

        return ResponseEntity.status(HttpStatus.OK).body(page);
    }

    private <T> Page<T> getPage(List<T> list, Pageable pageable) {
        Page<T> page = null;
        int pn = pageable.getPageNumber();
        int ps = pageable.getPageSize();
        int from = (pn * ps) > list.size() ? list.size() : (pn * ps);
        int to = (((pn + 1) * ps) > list.size() ? list.size() : ((pn + 1) * ps));

        page = new PageImpl<T>(list.subList(from, to), pageable, list.size());

        return page;
    }

    /**
     * @return
     */
    @RequestMapping(value = "/nginx-test", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @Secured({AuthoritiesConstants.USER, AuthoritiesConstants.ADMIN})
    public ResponseEntity nginxTestLongPoll() {
        String result = "OK";
        try {

            log.info("NINGIX TEST START");
            Thread.sleep(900 * 1000L);
            log.info("NINGIX TEST END");
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    /**
     * @return
     */
    @RequestMapping(value = "/clear-cache", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    @Secured({AuthoritiesConstants.USER, AuthoritiesConstants.ADMIN})
    public ResponseEntity clearCache() {
        String result = "OK";
        try {
            log.info("CACHE CLEAR START");
            Cache cache = cacheManager.getCache(CacheConfig.ICacheNames.HOURLY);
            cache.clear();
            cache = cacheManager.getCache(CacheConfig.ICacheNames.DAILY);
            cache.clear();
            cache = cacheManager.getCache(CacheConfig.ICacheNames.WEEKLY);
            cache.clear();
            log.info("CACHE CLEAR END");
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
