package com.swivel.ignite.auth.dto.request;

import com.swivel.ignite.auth.entity.Role;
import com.swivel.ignite.auth.enums.RoleType;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for User registration request
 */
@Getter
@Setter
public class UserRegistrationRequestDto implements RequestDto {

    private String username;
    private String password;
    private RoleType roleType;
    private Role role;

    @Override
    public String toLogJson() {
        return toJson();
    }

    @Override
    public boolean isRequiredAvailable() {
        return isNonEmpty(username) && isNonEmpty(password) && roleType != null && isNonEmpty(roleType.name())
                && roleType != RoleType.ADMIN;
    }

    @Override
    public boolean isNonEmpty(String field) {
        return field != null && !field.trim().isEmpty();
    }
}
