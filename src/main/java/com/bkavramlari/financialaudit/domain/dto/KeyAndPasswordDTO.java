package com.bkavramlari.financialaudit.domain.dto;

import lombok.Data;
import lombok.ToString;

/**
 * View Model object for storing the user's key and password.
 */
@Data
@ToString
public class KeyAndPasswordDTO {

    private String key;
    private String newPassword;

}
