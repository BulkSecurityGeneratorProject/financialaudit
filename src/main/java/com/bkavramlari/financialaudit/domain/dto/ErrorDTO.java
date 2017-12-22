package com.bkavramlari.financialaudit.domain.dto;

import lombok.Data;

import java.io.Serializable;


@Data
public class ErrorDTO implements Serializable {

    private String error;
    private String error_description;

    public ErrorDTO() {

    }

    public ErrorDTO(String error, String error_description) {
        this.setError(error);
        this.setError_description(error_description);
    }
}