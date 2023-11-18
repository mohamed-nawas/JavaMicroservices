package com.swivel.ignite.apigateway.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

/**
 * Cors Configuration
 */
@Configuration
public class SimpleCorsConfiguration extends CorsConfiguration {

    private final String allowedOrigins;
    private final String allowedMethods;
    private final long maxAge;
    private final String allowedHeaders;

    public SimpleCorsConfiguration(@Value("${accessControl.allowedOrigins}") String allowedOrigins,
                                   @Value("${accessControl.allowedMethods}") String allowedMethods,
                                   @Value("${accessControl.maxAge}") long maxAge,
                                   @Value("${accessControl.allowedHeaders}") String allowedHeaders) {
        this.allowedOrigins = allowedOrigins;
        this.allowedMethods = allowedMethods;
        this.maxAge = maxAge;
        this.allowedHeaders = allowedHeaders;
    }

    @Bean
    public CorsWebFilter corsWebFilter() {
        final SimpleCorsConfiguration corsConfig = new SimpleCorsConfiguration(allowedOrigins, allowedMethods,
                maxAge, allowedHeaders);
        corsConfig.setAllowedOrigins(Collections.singletonList(allowedOrigins));
        corsConfig.setAllowedMethods(Arrays.asList(allowedMethods.split(", ")));
        corsConfig.setMaxAge(maxAge);
        corsConfig.addAllowedHeader(allowedHeaders);

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }
}
