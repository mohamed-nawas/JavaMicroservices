package com.swivel.ignite.auth.enums;

/**
 * Enum values for role type
 */
public enum RoleType {

    ADMIN("ADMIN"),
    STUDENT("STUDENT");

    private final String type;

    RoleType(String type) {
        this.type = type;
    }
}
