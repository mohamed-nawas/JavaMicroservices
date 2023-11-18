package com.swivel.ignite.payment.exception;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.io.IOException;

/**
 * Student Microservice client error exception
 */
public class StudentServiceHttpClientErrorException extends HttpClientErrorException {

    public final transient JsonNode responseBody;
    public final Exception exception;

    public StudentServiceHttpClientErrorException(HttpStatus statusCode, String statusText, String responseBody,
                                                  Exception e) throws IOException {
        super(statusCode, statusText);
        this.responseBody = getJsonObject(responseBody);
        this.exception = e;
    }

    public StudentServiceHttpClientErrorException(HttpStatus status, String message) {
        super(status, message);
        this.responseBody = null;
        this.exception = null;

    }

    private JsonNode getJsonObject(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(jsonString);
    }
}
