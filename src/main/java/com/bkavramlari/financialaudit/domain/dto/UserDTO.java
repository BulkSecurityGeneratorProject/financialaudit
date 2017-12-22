package com.bkavramlari.financialaudit.domain.dto;

import com.bkavramlari.financialaudit.domain.security.Authority;
import com.bkavramlari.financialaudit.domain.security.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A DTO representing a user, with his authorities.
 */
@Data
@ToString
@EqualsAndHashCode
public class UserDTO {

    private Long id;

    @NotBlank
    @Size(min = 1, max = 50)
    private String login;

    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    private String lastName;

    @Email
    @Size(min = 5, max = 100)
    private String email;

    @Size(max = 256)
    private String imageUrl;

    private boolean activated;

    @Size(min = 2, max = 5)
    private String langKey;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private Set<String> authorities;

    /**
     *
     */
    public UserDTO() {
        // Empty constructor needed for Jackson.
    }

    /**
     * @param user
     */
    public UserDTO(User user) {
        this(user.getId(), user.getLogin(), user.getFirstName(), user.getLastName(),
                user.getEmail(), user.isActivated(), user.getImageUrl(), user.getLangKey(),
                user.getCreatedBy(), Instant.ofEpochMilli(user.getCreatedDate().getTime())
                , user.getLastModifiedBy(), Instant.ofEpochMilli(user.getLastModifiedDate().getTime()),
                user.getAuthorities().stream().map(Authority::getName)
                        .collect(Collectors.toSet()));
    }

    /**
     * @param id
     * @param login
     * @param firstName
     * @param lastName
     * @param email
     * @param activated
     * @param imageUrl
     * @param langKey
     * @param createdBy
     * @param createdDate
     * @param lastModifiedBy
     * @param lastModifiedDate
     * @param authorities
     */
    public UserDTO(Long id, String login, String firstName, String lastName,
                   String email, boolean activated, String imageUrl, String langKey,
                   String createdBy, Instant createdDate, String lastModifiedBy, Instant lastModifiedDate,
                   Set<String> authorities) {

        this.id = id;
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.activated = activated;
        this.imageUrl = imageUrl;
        this.langKey = langKey;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.lastModifiedBy = lastModifiedBy;
        this.lastModifiedDate = lastModifiedDate;
        this.authorities = authorities;
    }

    public static UserDTO toDTO(User entity) {

        UserDTO dto = new UserDTO();
        dto.setId(entity.getId());
        dto.setLogin(entity.getLogin());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setEmail(entity.getEmail());
        dto.setImageUrl(entity.getImageUrl());
        dto.setLangKey(entity.getLangKey());
        dto.setActivated(entity.isActivated());

        return dto;
    }


    public static User toEntity(UserDTO dto, User entity) {

        entity = entity == null ? new User() : entity;
        entity.setId(dto.getId());
        entity.setLogin(dto.getLogin());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());
        entity.setImageUrl(dto.getImageUrl());
        entity.setLangKey(dto.getLangKey());
        entity.setCreatedBy(dto.getCreatedBy());
        entity.setCreatedBy(dto.getCreatedBy());
        entity.setCreatedBy(dto.getCreatedBy());

        return entity;
    }
}
