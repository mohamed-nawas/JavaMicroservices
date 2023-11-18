package com.swivel.ignite.discovery;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This class tests {@link DiscoveryApplication} class
 */
@SpringBootTest
class DiscoveryApplicationTest {

    /**
     * This method tests Spring application main run method
     */
    @Test
    void Should_RunSpringApplication() {
        DiscoveryApplication.main(new String[]{});

        assertTrue(true, "Spring Application Context Loaded Successfully");
    }
}
