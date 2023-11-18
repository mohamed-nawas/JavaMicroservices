package com.swivel.ignite.auth.exception;

/**
 * OpenFashion auth service exception
 */
public class AuthException extends RuntimeException {

    /**
     * OpenFashion auth service exception with error msg
     *
     * @param message message
     */
    public AuthException(String message) {
        super(message);
    }

    /**
     * OpenFashion auth service exception with error msg and throwable error
     *
     * @param message message
     * @param error   error
     */
    public AuthException(String message, Throwable error) {
        super(message, error);
    }
}
