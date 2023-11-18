package com.swivel.ignite.student.exception;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;

/**
 * Payment Microservice client error exception
 */
public class PaymentServiceHttpClientErrorException extends HttpClientErrorException {

    public final transient JsonNode responseBody;
    public final Exception exception;

    public PaymentServiceHttpClientErrorException(HttpStatus statusCode, String statusText, String responseBody,
                                                  Exception e) throws IOException {
        super(statusCode, statusText);
        this.responseBody = getJsonObject(responseBody);
        this.exception = e;
    }

    public PaymentServiceHttpClientErrorException(HttpStatus statusCode, String message) throws IOException {
        super(statusCode, message);
        this.responseBody = null;
        this.exception = null;
    }

    private JsonNode getJsonObject(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(jsonString);
    }
}
