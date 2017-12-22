package com.bkavramlari.financialaudit.resource.financial;

import com.bkavramlari.financialaudit.domain.enums.UploadFileProcStats;
import com.bkavramlari.financialaudit.domain.enums.UploadFileTypes;
import com.bkavramlari.financialaudit.security.AuthoritiesConstants;
import com.bkavramlari.financialaudit.service.financial.UploadService;
import com.bkavramlari.financialaudit.service.financial.parser.csv.exceptions.CsvParseServiceException;
import com.bkavramlari.financialaudit.service.financial.parser.xml.exceptions.XmlParseServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by benan on 10/1/2015.
 */
@Slf4j
@Controller
@RequestMapping("/api/files")
//@MultipartConfig(fileSizeThreshold = 10 * 1024, maxFileSize = 10 * 1024, maxRequestSize = 10 * 1024)
public class UploadController {

    @Value("${upload.UPLOAD_DIRECTORY}")
    private String mainDirectory;

    @Value("${upload.PUBLIC_PATH}")
    private String publicPath;

    @Value("${upload.ALLOWED_EXTENSIONS}")
    private String allowedExtensionsString;

    @Autowired
    private UploadService uploadService;

    List<String> allowedExtensions = new ArrayList<>();

    @RequestMapping(value = "/parse", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Secured({AuthoritiesConstants.USER, AuthoritiesConstants.ADMIN})
    public ResponseEntity uploadedFilesParseProcess() {
        log.info("*** UPLOADED FILES PARSE SERVICE STARTED");
        HashMap<String, Object> map = new HashMap<>();
        HttpStatus status = HttpStatus.OK;
        map.put("result", "OK");
        ResponseEntity responseEntity = null;
        try {
            uploadService.parseProcess();
        } catch (CsvParseServiceException e) {
            log.error(e.getMessage());
            map.put("result", e.getMessage());
            status = HttpStatus.CONFLICT;
        } catch (XmlParseServiceException e) {
            log.error(e.getMessage());
            map.put("result", e.getMessage());
            status = HttpStatus.CONFLICT;
        } catch (Exception e) {
            log.error(e.getMessage());
            map.put("result", e.getMessage());
            status = HttpStatus.CONFLICT;
        }
        log.info("*** UPLOADED FILES PARSE SERVICE ENDED");

        return ResponseEntity.status(status)
                .body(map);
    }

    @RequestMapping(value = "/upload/{type}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Secured({AuthoritiesConstants.USER, AuthoritiesConstants.ADMIN})
    public ResponseEntity<HashMap<String, Object>> saveFile(@RequestParam("file") MultipartFile multipartFile,
                                                            @PathVariable("type") UploadFileTypes fileType) throws Exception {
        ResponseEntity<HashMap<String, Object>> response = null;
        HashMap<String, Object> map = new HashMap<>();
        String folderName = mainDirectory + publicPath;
        String originalFileName = multipartFile.getOriginalFilename().trim();
        String extension = FilenameUtils.getExtension(originalFileName).toLowerCase();
        String fileName = DigestUtils.md5DigestAsHex((System.currentTimeMillis() + "" + new Random().nextLong()).getBytes()) + "." + extension;
        // File extension control
        if (!allowedExtensions.contains(extension)) {
            map.put("result", "Yuklemeniz basarisizdir. Gecerli dosya tipleri " + allowedExtensionsString + " dir. "
                    + "Sizin yuklemek istediginiz dosya tipi ise: " + extension + " 'dir ve gecersizdir.");
            return new ResponseEntity<>(map, HttpStatus.CONFLICT);
        }
        // File upload
        Boolean res = uploadFile(multipartFile, fileName, publicPath, folderName, map);
        response = new ResponseEntity<>(map, HttpStatus.CONFLICT);
        if (res) {
            Object upload = uploadService.save(fileName, extension, publicPath, originalFileName, fileType, UploadFileProcStats.NEW);
            if (upload == null) {
                map.put("result", "Dosya yuklendi ancak database kayit atilamadi.");
            }
            response = new ResponseEntity<>(map, upload != null ? HttpStatus.ACCEPTED : HttpStatus.CONFLICT);
        }

        return response;
    }

    /**
     * @param multipartFile
     * @param path
     * @param folderName
     * @param map
     * @return
     */
    private Boolean uploadFile(MultipartFile multipartFile, String name, String path, String folderName, HashMap<String, Object> map) {
        if (!multipartFile.isEmpty()) {
            try {
                byte[] bytes = multipartFile.getBytes();
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(folderName + new File(name)));
                stream.write(bytes);
                stream.close();
                map.put("result", path + name);
                log.debug("creating file: " + name);

                return true;
            } catch (Exception e) {
                map.put("result", "You failed to upload " + name + " => " + e.getMessage());
                return false;

            }
        } else {
            map.put("result", "You failed to upload " + name + " => " + "because the file was empty.");
            return false;

        }
    }

    /**
     * Eger dir yoksa var edilir...
     */
    @PostConstruct
    public void createFolder() {
        String folderName = mainDirectory + publicPath;
        createFolder(folderName);
        setAllowedExtensions();
    }

    /**
     * @param directoryName
     */
    public void createFolder(String directoryName) {

        File theDir = new File(directoryName);

        if (!theDir.exists()) {
            log.debug("creating directory: " + directoryName);
            boolean result = false;
            try {
                theDir.mkdirs();
                result = true;
            } catch (SecurityException se) {
                log.error(se.getMessage(), se);
            }
            if (result) {
                log.debug("DIR created");
            }
        }

    }

    /**
     *
     */
    public void setAllowedExtensions() {
        String[] aes = allowedExtensionsString.split(",");
        for (String ext : aes) {
            if (ext != null && !ext.trim().equals("")) {
                allowedExtensions.add(ext.trim().toLowerCase());
            }
        }
    }

}
