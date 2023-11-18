package com.swivel.ignite.payment.wrapper;

import com.swivel.ignite.payment.dto.response.ResponseDto;
import com.swivel.ignite.payment.enums.ResponseStatusType;
import lombok.Getter;

/**
 * Error response wrapper
 */
@Getter
public class ErrorResponseWrapper extends ResponseWrapper {

    private final ResponseDto data;
    private final int errorCode;

    /**
     * @param status         status
     * @param message        dev message
     * @param data           data
     * @param displayMessage display message
     * @param errorCode      error code
     */
    public ErrorResponseWrapper(ResponseStatusType status, String message, ResponseDto data, String displayMessage,
                                int errorCode) {
        super(status, message, displayMessage);
        this.data = data;
        this.errorCode = errorCode;
    }
}
