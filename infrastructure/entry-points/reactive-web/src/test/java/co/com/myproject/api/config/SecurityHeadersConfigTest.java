package co.com.myproject.api.config;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

public class SecurityHeadersConfigTest {

    @Test
    void filter_shouldAddSecurityHeaders() {
        // Mocks
        ServerWebExchange exchange = mock(ServerWebExchange.class);
        ServerHttpResponse response = mock(ServerHttpResponse.class);
        HttpHeaders headers = new HttpHeaders();

        when(exchange.getResponse()).thenReturn(response);
        when(response.getHeaders()).thenReturn(headers);

        WebFilterChain chain = mock(WebFilterChain.class);
        when(chain.filter(exchange)).thenReturn(Mono.empty());

        // Clase bajo prueba
        SecurityHeadersConfig filter = new SecurityHeadersConfig();

        // Ejecutamos el filtro
        Mono<Void> result = filter.filter(exchange, chain);

        // Verificamos que el flujo se complete correctamente
        StepVerifier.create(result)
                .verifyComplete();

        // Verificamos que las cabeceras hayan sido seteadas correctamente
        assert headers.getFirst("Content-Security-Policy").equals("default-src 'self'; frame-ancestors 'self'; form-action 'self'");
        assert headers.getFirst("Strict-Transport-Security").equals("max-age=31536000;");
        assert headers.getFirst("X-Content-Type-Options").equals("nosniff");
        assert headers.getFirst("Server").equals("");
        assert headers.getFirst("Cache-Control").equals("no-store");
        assert headers.getFirst("Pragma").equals("no-cache");
        assert headers.getFirst("Referrer-Policy").equals("strict-origin-when-cross-origin");

        // Verificamos que el chain.filter fue llamado una vez
        verify(chain, times(1)).filter(exchange);
    }
}
