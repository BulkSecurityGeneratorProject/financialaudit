package com.bkavramlari.financialaudit.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.Set;

@Data
@ToString
@EqualsAndHashCode
public class UserProfileDTO extends UserDTO {

    public static final int PASSWORD_MIN_LENGTH = 4;

    public static final int PASSWORD_MAX_LENGTH = 100;

    @Size(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH)
    private String password;

    public UserProfileDTO() {
        // Empty constructor needed for Jackson.
    }

    public UserProfileDTO(Long id, String login, String password, String firstName, String lastName,
                          String email, boolean activated, String imageUrl, String langKey,
                          String createdBy, Instant createdDate, String lastModifiedBy, Instant lastModifiedDate,
                          Set<String> authorities) {

        super(id, login, firstName, lastName, email, activated, imageUrl, langKey,
                createdBy, createdDate, lastModifiedBy, lastModifiedDate, authorities);

        this.password = password;
    }
}
