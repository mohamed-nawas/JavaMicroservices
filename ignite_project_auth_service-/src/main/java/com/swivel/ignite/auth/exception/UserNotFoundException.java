package com.swivel.ignite.auth.exception;

/**
 * UserNotFoundException exception
 */
public class UserNotFoundException extends RuntimeException {

    /**
     * UserNotFoundException exception with error msg
     *
     * @param message message
     */
    public UserNotFoundException(String message) {
        super(message);
    }

    /**
     * UserNotFoundException exception with error msg and throwable error
     *
     * @param message message
     * @param error   error
     */
    public UserNotFoundException(String message, Throwable error) {
        super(message, error);
    }
}
