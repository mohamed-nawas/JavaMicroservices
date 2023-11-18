package com.swivel.ignite.auth.enums;

import lombok.Getter;

/**
 * Enum values for Success Response
 */
@Getter
public enum SuccessResponseStatusType {

    CREATE_USER(201, "Successfully created the user"),
    LOGIN_USER(200, "Successfully logged in the user"),
    DELETE_USER(202, "Successfully deleted the user");

    private final int code;
    private final String message;

    SuccessResponseStatusType(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
