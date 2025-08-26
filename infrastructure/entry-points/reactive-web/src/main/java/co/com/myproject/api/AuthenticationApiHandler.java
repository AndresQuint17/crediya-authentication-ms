package co.com.myproject.api;

import co.com.myproject.api.dto.RegisterUserDto;
import co.com.myproject.api.mapper.UserMapper;
import co.com.myproject.usecase.registerUser.RegisterUserUseCase;
import co.com.myproject.usecase.updateUser.UpdateUserUseCase;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AuthenticationApiHandler {
    private final RegisterUserUseCase registerUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final UserMapper userMapper;
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationApiHandler.class);

    public Mono<ServerResponse> listenRegisterUser(ServerRequest serverRequest) {
        logger.info("Enter a new request to register a user.");
        return serverRequest.bodyToMono(RegisterUserDto.class)
                .doOnNext(dto -> logger.info("Request body deserialized successfully: {}", dto))
                .flatMap(dto -> {
                    // Realizamos la validación manual aquí
                    logger.debug("Validating RegisterUserDto: {}", dto);
                    Set<ConstraintViolation<RegisterUserDto>> violations = validator.validate(dto);
                    if (!violations.isEmpty()) {
                        // Si hay errores, devolvemos un error 400 con los detalles
                        String errorMessage = violations.stream()
                                .map(ConstraintViolation::getMessage)
                                .collect(Collectors.joining(", "));
                        logger.warn("Validation failed: {}", errorMessage);
                        return ServerResponse
                                .badRequest()
                                .bodyValue("Validations failed: " + errorMessage);
                    }

                    logger.info("Init validation passed.");
                    // Si la validación pasa, procedemos con el flujo
                    return Mono.just(dto)
                            .map(userMapper::toModel)
                            .doOnNext(model -> logger.debug("Mapped DTO to domain model: {}", model))
                            .flatMap(registerUserUseCase::registerUser)
                            .doOnNext(registeredUser -> logger.info("User registered successfully: {}", registeredUser))
                            .map(userMapper::toDto)
                            .doOnNext(responseDto -> logger.debug("Mapped domain model back to DTO: {}", responseDto))
                            .flatMap(userDto -> {
                                logger.info("Sending response with HTTP status 201 Created.");
                                return ServerResponse.status(HttpStatus.CREATED)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .bodyValue(userDto);
                            });
                });
    }

    public Mono<ServerResponse> listenUpdateUser(ServerRequest serverRequest) {
        return ServerResponse.ok().bodyValue("TODO: Pending implementation");
    }

    public Mono<ServerResponse> listenHelloEndpoint(ServerRequest serverRequest) {
        return ServerResponse.ok().bodyValue("HELLO");
    }
}
