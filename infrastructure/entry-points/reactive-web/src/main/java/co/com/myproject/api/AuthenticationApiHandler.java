package co.com.myproject.api;

import co.com.myproject.api.dto.LoginDto;
import co.com.myproject.api.dto.RegisterUserDto;
import co.com.myproject.api.mapper.UserMapper;
import co.com.myproject.usecase.finduserbyid.FindUserByIdCardUseCase;
import co.com.myproject.usecase.login.LoginUseCase;
import co.com.myproject.usecase.registerUser.RegisterUserUseCase;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final FindUserByIdCardUseCase findUserByIdCardUseCase;
    private final LoginUseCase loginUseCase;
    private final UserMapper userMapper;
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationApiHandler.class);
    private String errorMessage = "";

    @PreAuthorize("hasAnyRole('ADMIN', 'ASESOR')")
    public Mono<ServerResponse> listenRegisterUser(ServerRequest serverRequest) {
        logger.info("Enter a new request to register a user.");
        return serverRequest.bodyToMono(RegisterUserDto.class)
                .doOnNext(dto -> logger.info("Request body deserialized successfully: {}", dto))
                .flatMap(dto -> {
                    if (!isValidRequest(dto)) {
                        return ServerResponse
                                .badRequest()
                                .bodyValue("Validations failed: " + this.errorMessage);
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

    //@PreAuthorize("hasAuthority('CLIENTE')") proteger metodo por authority
    //@PreAuthorize("hasAnyAuthority('CLIENTE', 'ADMIN')") proteger metodo por grupo de authorities
    @PreAuthorize("hasRole('CLIENTE')")
    public Mono<ServerResponse> listenGetByIdCardEndpoint(ServerRequest serverRequest) {
        logger.info("Enter a new request to find user by id card.");
        String idCard = serverRequest.pathVariable("idCard");
        return findUserByIdCardUseCase.findUserByIdCard(idCard)
                .map(userMapper::toFindByIdResponseDto)
                .flatMap(user -> {
                    logger.info("✅ Find user by id card use case execution completed.");
                    return ServerResponse.ok().bodyValue(user);
                })
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> listenLogin(ServerRequest serverRequest) {
        logger.info("Enter a new request to login use case.");
        return serverRequest.bodyToMono(LoginDto.class)
                .doOnNext(dto -> logger.debug("🔍 Cuerpo deserializado correctamente: " + dto))
                .flatMap(dto -> {
                            logger.debug("🔍 Llamando al caso de uso de autenticación...");

                        return loginUseCase.authenticate(dto.email(), dto.password())
                                .flatMap(token -> {
                                    logger.info("✅ Login use case execution completed.");
                                    return ServerResponse.ok().bodyValue(token);
                                });
                })
                .doOnError(error -> logger.error("❌ Error en el flujo: " + error.getMessage()))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    private boolean isValidRequest(RegisterUserDto dto) {
        // Realizamos la validación del request aquí
        logger.debug("Validating RegisterUserDto: {}", dto);
        Set<ConstraintViolation<RegisterUserDto>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            // Si hay errores, devolvemos un error 400 con los detalles
            this.errorMessage = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));
            logger.warn("Validation failed: {}", this.errorMessage);
            return false;
        }
        return true;
    }
}
