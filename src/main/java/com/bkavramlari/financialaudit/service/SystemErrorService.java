package com.bkavramlari.financialaudit.service;

import com.bkavramlari.financialaudit.domain.dto.SystemErrorDTO;
import com.bkavramlari.financialaudit.domain.error.SystemError;
import com.bkavramlari.financialaudit.repository.SystemErrorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class SystemErrorService {

    @Autowired
    private SystemErrorRepository systemErrorRepository;


    @Transactional
    public void save(SystemErrorDTO systemErrorDTO) {

        SystemError systemError = SystemErrorDTO.toEntity(systemErrorDTO);
        systemErrorRepository.save(systemError);
    }
}

