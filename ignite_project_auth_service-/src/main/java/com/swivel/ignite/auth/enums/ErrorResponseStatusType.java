package com.swivel.ignite.auth.enums;

import lombok.Getter;

/**
 * Enum values for Error Response
 */
@Getter
public enum ErrorResponseStatusType {

    MISSING_REQUIRED_FIELDS(400, "Missing required fields"),
    ROLE_NOT_FOUND(404, "Role not found"),
    USER_ALREADY_EXISTS(400, "User already exists, please login"),
    INVALID_LOGIN(401, "Incorrect username or password"),
    USER_NOT_FOUND(404, "User not found for given username"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error");

    private final int code;
    private final String message;

    ErrorResponseStatusType(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
