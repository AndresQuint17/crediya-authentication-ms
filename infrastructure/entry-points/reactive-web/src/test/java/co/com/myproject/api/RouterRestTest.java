package co.com.myproject.api;

import co.com.myproject.api.dto.RegisterUserDto;
import co.com.myproject.api.exception.GlobalErrorHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;

class RouterRestTest {

    private WebTestClient webTestClient;
    private AuthenticationApiHandler handler;
    private GlobalErrorHandler globalErrorHandler;

    @BeforeEach
    void setUp() {
        handler = Mockito.mock(AuthenticationApiHandler.class);
        globalErrorHandler = new GlobalErrorHandler();

        RouterRest routerRest = new RouterRest();
        webTestClient = WebTestClient
                .bindToRouterFunction(routerRest.routerFunction(handler, globalErrorHandler))
                .configureClient()
                .baseUrl("/api/v1")
                .build();
    }

    @Test
    void testHelloEndpoint() {
        Mockito.when(handler.listenHelloEndpoint(Mockito.any()))
                .thenReturn(ServerResponse.ok().bodyValue("Hello World"));

        webTestClient.get()
                .uri("/hello")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("Hello World");
    }

    @Test
    void testRegisterUserEndpoint() {
        RegisterUserDto request = new RegisterUserDto(
                "Andres",
                "Quintero",
                "123456789",
                LocalDate.of(1990, 1, 1),
                "Calle falsa 123",
                "3015484104",
                "test@example.com",
                BigDecimal.valueOf(5000000)
        );

        Mockito.when(handler.listenRegisterUser(Mockito.any()))
                .thenReturn(ServerResponse.created(null).bodyValue(request));

        webTestClient.post()
                .uri("/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), RegisterUserDto.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(RegisterUserDto.class)
                .isEqualTo(request);
    }

    @Test
    void testUpdateUserEndpoint() {
        Mockito.when(handler.listenUpdateUser(Mockito.any()))
                .thenReturn(ServerResponse.ok().bodyValue("User updated"));

        webTestClient.get()
                .uri("/usuarios/update")
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("User updated");
    }
}
