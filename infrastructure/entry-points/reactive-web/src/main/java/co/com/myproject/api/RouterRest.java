package co.com.myproject.api;

import co.com.myproject.api.dto.RegisterUserDto;
import co.com.myproject.api.exception.GlobalErrorHandler;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Crediya Authentication MS",
                version = "1.0.0",
                description = "API for authenticate user from Crediya system."
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Local development server")
        }
)
public class RouterRest {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationApiHandler.class);

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/solicitud",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.POST,
                    beanClass = AuthenticationApiHandler.class,
                    beanMethod = "listenRegisterUserUseCase",
                    operation = @Operation(
                            operationId = "listenRegisterUserUseCase",
                            summary = "Register a new user to the system",
                            tags = {"Authenticate User"},
                            requestBody = @RequestBody(
                                    required = true,
                                    content = @Content(
                                            schema = @Schema(implementation = RegisterUserDto.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "User created successfully",
                                            content = @Content(
                                                    schema = @Schema(implementation = RegisterUserDto.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400",
                                            description = "Invalid request payload"
                                    )
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunction(AuthenticationApiHandler handler, GlobalErrorHandler globalErrorHandler) {

        return RouterFunctions.route()
                .POST("/api/v1/usuarios", handler::listenRegisterUser)
                .GET("/api/v1/hello", handler::listenHelloEndpoint)
                .GET("/api/v1/usuarios/update", handler::listenUpdateUser)
                .build()
                .filter((request, next) ->
                        next.handle(request)
                                .onErrorResume(error -> {
                                    logger.error(error.getMessage());
                                    return globalErrorHandler.handleError(error, request);
                                })
                );
    }
}
