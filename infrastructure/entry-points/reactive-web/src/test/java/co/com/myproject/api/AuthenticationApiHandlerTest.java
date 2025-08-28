package co.com.myproject.api;

import co.com.myproject.api.mapper.UserMapper;
import co.com.myproject.api.dto.RegisterUserDto;
import co.com.myproject.model.user.User;
import co.com.myproject.usecase.finduserbyid.FindUserByIdCardUseCase;
import co.com.myproject.usecase.registerUser.RegisterUserUseCase;
import co.com.myproject.usecase.updateUser.UpdateUserUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class AuthenticationApiHandlerTest {

    private RegisterUserUseCase registerUserUseCase;
    private UserMapper userMapper;
    private AuthenticationApiHandler handler;
    private FindUserByIdCardUseCase findUserByIdCardUseCase;

    @BeforeEach
    void setUp() {
        registerUserUseCase = mock(RegisterUserUseCase.class);
        UpdateUserUseCase updateUserUseCase = mock(UpdateUserUseCase.class);
        userMapper = mock(UserMapper.class);
        handler = new AuthenticationApiHandler(registerUserUseCase, updateUserUseCase, findUserByIdCardUseCase, userMapper);
    }

    private RegisterUserDto buildValidDto() {
        return new RegisterUserDto(
                "John",
                "Doe",
                "102365482",
                LocalDate.of(1990, 1, 1),
                "123 Main St",
                "1234567890",
                "john.doe@example.com",
                BigDecimal.valueOf(1000)
        );
    }

    @Test
    void listenRegisterUser_ShouldReturn201_WhenValidRequest() throws Exception {
        // given
        RegisterUserDto dto = buildValidDto();
        User domainUser = new User();
        RegisterUserDto responseDto = buildValidDto();

        when(userMapper.toModel(dto)).thenReturn(domainUser);
        when(registerUserUseCase.registerUser(domainUser)).thenReturn(Mono.just(domainUser));
        when(userMapper.toDto(domainUser)).thenReturn(responseDto);

        // Serializamos el DTO a JSON para simular body real
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        String jsonBody = objectMapper.writeValueAsString(dto);

        // Construimos un HTTP request falso con body
        MockServerHttpRequest httpRequest = MockServerHttpRequest.post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonBody);

        // Creamos el exchange y el ServerRequest
        MockServerWebExchange exchange = MockServerWebExchange.from(httpRequest);
        ServerRequest request = ServerRequest.create(exchange, HandlerStrategies.withDefaults().messageReaders());

        // when
        Mono<ServerResponse> responseMono = handler.listenRegisterUser(request);

        // then
        StepVerifier.create(responseMono)
                .assertNext(response -> {
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED);
                    assertThat(response.headers().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);

                    // (opcional) Validar que el body contiene el DTO esperado
                    // Puedes deserializar la respuesta si quieres:
                    // response.bodyToMono(String.class).subscribe(System.out::println);
                })
                .verifyComplete();

        verify(registerUserUseCase).registerUser(domainUser);
        verify(userMapper).toDto(domainUser);
    }

    @Test
    void listenRegisterUser_ShouldReturn400_WhenInvalidRequest() throws Exception {
        // given → DTO inválido (nombre vacío)
        RegisterUserDto invalidDto = new RegisterUserDto(
                "",
                "Doe",
                "1025487954",
                LocalDate.of(1990, 1, 1),
                "123 Main St",
                "1234567890",
                "invalid-email",
                BigDecimal.valueOf(1000)
        );

        // Serializamos el DTO a JSON para simular body real
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        String jsonBody = objectMapper.writeValueAsString(invalidDto);

        // Construimos un HTTP request falso con body
        MockServerHttpRequest httpRequest = MockServerHttpRequest.post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .body(jsonBody);

        // Creamos el exchange y el ServerRequest
        MockServerWebExchange exchange = MockServerWebExchange.from(httpRequest);
        ServerRequest request = ServerRequest.create(exchange, HandlerStrategies.withDefaults().messageReaders());

        // when
        Mono<ServerResponse> responseMono = handler.listenRegisterUser(request);

        // then
        StepVerifier.create(responseMono)
                .assertNext(response -> {
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                })
                .verifyComplete();

        verifyNoInteractions(registerUserUseCase);
    }

    @Test
    void listenHelloEndpoint_ShouldReturnHello() {
        // given
        ServerRequest request = mock(ServerRequest.class);

        // when
        Mono<ServerResponse> responseMono = handler.listenHelloEndpoint(request);

        // then
        StepVerifier.create(responseMono)
                .assertNext(response -> {
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
                })
                .verifyComplete();
    }

    @Test
    void listenUpdateUser_ShouldReturnTodoMessage() {
        // given
        ServerRequest request = mock(ServerRequest.class);

        // when
        Mono<ServerResponse> responseMono = handler.listenUpdateUser(request);

        // then
        StepVerifier.create(responseMono)
                .assertNext(response -> {
                    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK);
                })
                .verifyComplete();
    }
}