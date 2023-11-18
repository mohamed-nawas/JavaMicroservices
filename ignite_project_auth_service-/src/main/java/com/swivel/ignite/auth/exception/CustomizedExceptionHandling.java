package com.swivel.ignite.auth.exception;

import com.swivel.ignite.auth.enums.ErrorResponseStatusType;
import com.swivel.ignite.auth.enums.ResponseStatusType;
import com.swivel.ignite.auth.wrapper.ErrorResponseWrapper;
import com.swivel.ignite.auth.wrapper.ResponseWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomizedExceptionHandling extends ResponseEntityExceptionHandler {

    private static final String ERROR_MESSAGE = "Oops!! Something went wrong. Please try again.";

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ResponseWrapper> handleAuthException(AuthException exception, WebRequest request) {
        ResponseWrapper responseWrapper = new ErrorResponseWrapper(ResponseStatusType.ERROR, ErrorResponseStatusType
                .INTERNAL_SERVER_ERROR.getMessage(), null, ERROR_MESSAGE, ErrorResponseStatusType
                .INTERNAL_SERVER_ERROR.getCode());
        return new ResponseEntity<>(responseWrapper, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserRoleNotFoundException.class)
    public ResponseEntity<ResponseWrapper> handleUserRoleNotFoundException(UserRoleNotFoundException exception,
                                                                           WebRequest request) {
        ResponseWrapper responseWrapper = new ErrorResponseWrapper(ResponseStatusType.ERROR, ErrorResponseStatusType
                .ROLE_NOT_FOUND.getMessage(), null, ERROR_MESSAGE, ErrorResponseStatusType
                .ROLE_NOT_FOUND.getCode());
        return new ResponseEntity<>(responseWrapper, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ResponseWrapper> handleUserAlreadyExistsException(UserAlreadyExistsException exception,
                                                                            WebRequest request) {
        ResponseWrapper responseWrapper = new ErrorResponseWrapper(ResponseStatusType.ERROR, ErrorResponseStatusType
                .USER_ALREADY_EXISTS.getMessage(), null, ERROR_MESSAGE, ErrorResponseStatusType
                .USER_ALREADY_EXISTS.getCode());
        return new ResponseEntity<>(responseWrapper, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ResponseWrapper> handleUserNotFoundException(UserNotFoundException exception,
                                                                       WebRequest request) {
        ResponseWrapper responseWrapper = new ErrorResponseWrapper(ResponseStatusType.ERROR, ErrorResponseStatusType
                .USER_NOT_FOUND.getMessage(), null, ERROR_MESSAGE, ErrorResponseStatusType
                .USER_NOT_FOUND.getCode());
        return new ResponseEntity<>(responseWrapper, HttpStatus.BAD_REQUEST);
    }
}
