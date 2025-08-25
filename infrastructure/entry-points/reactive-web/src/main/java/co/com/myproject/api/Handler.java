package co.com.myproject.api;

import co.com.myproject.api.dto.RegisterUserDto;
import co.com.myproject.api.mapper.UserMapper;
import co.com.myproject.usecase.registerUser.RegisterUserUseCase;
import co.com.myproject.usecase.updateUser.UpdateUserUseCase;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class Handler {
    private final RegisterUserUseCase registerUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final UserMapper userMapper;
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public Mono<ServerResponse> listenRegisterUserUseCase(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(RegisterUserDto.class)
                .flatMap(dto -> {
                    // Realizamos la validación manual aquí
                    Set<ConstraintViolation<RegisterUserDto>> violations = validator.validate(dto);
                    if (!violations.isEmpty()) {
                        // Si hay errores, devolvemos un error 400 con los detalles
                        String errorMessage = violations.stream()
                                .map(ConstraintViolation::getMessage)
                                .collect(Collectors.joining(", "));
                        return ServerResponse.badRequest().bodyValue("Validations failed: " + errorMessage);
                    }

                    // Si la validación pasa, procedemos con el flujo
                    return Mono.just(dto)
                            .map(userMapper::toEntity)
                            .flatMap(registerUserUseCase::registerUser)
                            .map(userMapper::toDto)
                            .flatMap(userDto -> ServerResponse.status(HttpStatus.CREATED)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .bodyValue(userDto))
                            .onErrorResume(ex -> {
                                if (ex instanceof Exception) {
                                    return ServerResponse.status(400).bodyValue("Error message: " + ex.getMessage());
                                }
                                return ServerResponse.status(500).bodyValue("Unknown error");
                            });
                });
        /*return serverRequest.bodyToMono(RegisterUserDto.class)
                .map(userMapper::toEntity)
                .flatMap(registerUserUseCase::registerUser)
                .map(userMapper::toDto)
                .flatMap(dto -> ServerResponse.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(dto))
                .onErrorResume(ex -> {
                    if (ex instanceof Exception) {
                        return ServerResponse.status(400).bodyValue("Error message: " + ex.getMessage());
                    }
                    return ServerResponse.status(500).bodyValue("Error unknown");
                });*/
    }

    public Mono<ServerResponse> listenUpdateUserUseCase(ServerRequest serverRequest) {
        return ServerResponse.ok().bodyValue("All Good");
    }

    public Mono<ServerResponse> listenGETUseCase(ServerRequest serverRequest) {
        return ServerResponse.ok().bodyValue("HELLO");
    }
}
