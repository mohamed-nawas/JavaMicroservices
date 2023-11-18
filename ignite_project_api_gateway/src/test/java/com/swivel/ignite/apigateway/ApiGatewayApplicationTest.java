package com.swivel.ignite.apigateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This class tests {@link ApiGatewayApplication} class
 */
@SpringBootTest
class ApiGatewayApplicationTest {

    /**
     * This method tests Spring application main run method
     */
    @Test
    void Should_RunSpringApplication() {
        ApiGatewayApplication.main(new String[]{});

        assertTrue(true, "Spring Application Context Loaded Successfully");
    }
}
