package co.com.myproject.api.config;

import org.junit.jupiter.api.Test;
import org.springframework.web.cors.reactive.CorsWebFilter;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class CorsConfigTest {

    @Test
    void corsWebFilterCreation() {
        CorsConfig corsConfig = new CorsConfig();
        CorsWebFilter filter = corsConfig.corsWebFilter("http://allowed-origin.com");
        assertNotNull(filter);
    }
}