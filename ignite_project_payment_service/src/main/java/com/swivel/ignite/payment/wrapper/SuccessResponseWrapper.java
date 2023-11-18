package com.swivel.ignite.payment.wrapper;

import com.swivel.ignite.payment.dto.response.ResponseDto;
import com.swivel.ignite.payment.enums.ResponseStatusType;
import lombok.Getter;

/**
 * Success response wrapper
 */
@Getter
public class SuccessResponseWrapper extends ResponseWrapper {

    private final ResponseDto data;
    private final int statusCode;

    /**
     * @param status         status
     * @param message        dev message
     * @param data           data
     * @param displayMessage display message
     * @param statusCode     status code
     */
    public SuccessResponseWrapper(ResponseStatusType status, String message, ResponseDto data, String displayMessage,
                                  int statusCode) {
        super(status, message, displayMessage);
        this.data = data;
        this.statusCode = statusCode;
    }
}