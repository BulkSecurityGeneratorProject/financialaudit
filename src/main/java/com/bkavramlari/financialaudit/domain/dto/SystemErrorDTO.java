package com.bkavramlari.financialaudit.domain.dto;

import com.bkavramlari.financialaudit.domain.enums.ErrorType;
import com.bkavramlari.financialaudit.domain.error.SystemError;
import lombok.Data;


@Data
public class SystemErrorDTO {

    private Long id;
    private ErrorType errorType;
    private String username;
    private String errorDescription;


    public SystemErrorDTO(ErrorType errorType, String username){
        this.errorType = errorType;
        this.username = username;
    }

    public SystemErrorDTO(ErrorType errorType, String username, String errorDescription){
        this.errorType = errorType;
        this.username = username;
        this.errorDescription = errorDescription;
    }

    SystemErrorDTO(){
    }

    public static SystemError toEntity(SystemErrorDTO systemErrorDTO) {

        SystemError systemError = new SystemError();
        systemError.setId(systemErrorDTO.getId());
        systemError.setErrorType(systemErrorDTO.getErrorType());
        systemError.setUsername(systemErrorDTO.getUsername());
        systemError.setErrorDescription(systemErrorDTO.getErrorDescription());
        return systemError;

    }


}
