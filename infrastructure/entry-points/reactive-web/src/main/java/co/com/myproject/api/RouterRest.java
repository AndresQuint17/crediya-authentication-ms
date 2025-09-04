package co.com.myproject.api;

import co.com.myproject.api.dto.LoginDto;
import co.com.myproject.api.dto.RegisterUserDto;
import co.com.myproject.api.dto.ResponseFindIdCardDto;
import co.com.myproject.api.exception.GlobalErrorHandler;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
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
        },
        security = {
                @SecurityRequirement(name = "bearerAuth")
        }
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class RouterRest {
    private static final Logger logger = LoggerFactory.getLogger(RouterRest.class);

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/login",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.POST,
                    beanClass = AuthenticationApiHandler.class,
                    beanMethod = "listenLogin",
                    operation = @Operation(
                            operationId = "listenLogin",
                            summary = "Authenticate user and generate JWT",
                            tags = {"Login"},
                            requestBody = @RequestBody(
                                    required = true,
                                    content = @Content(
                                            schema = @Schema(implementation = LoginDto.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Login successful, JWT returned",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(
                                                            type = "object",
                                                            example = "{\"token\": \"eyJhbGciOiJIUzI1NiIsInR...\"}"
                                                    )
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "401",
                                            description = "Unauthorized - Invalid credentials"
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/usuarios",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.POST,
                    beanClass = AuthenticationApiHandler.class,
                    beanMethod = "listenRegisterUser",
                    operation = @Operation(
                            operationId = "listenRegisterUser",
                            summary = "Register a new user to the system",
                            tags = {"Register a new user"},
                            security = @SecurityRequirement(name = "bearerAuth"),
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
            ),
            @RouterOperation(
                    path = "/api/v1/usuarios/{idCard}",
                    produces = {MediaType.APPLICATION_JSON_VALUE},
                    method = RequestMethod.GET,
                    beanClass = AuthenticationApiHandler.class,
                    beanMethod = "listenGetByIdCardEndpoint",
                    operation = @Operation(
                            operationId = "listenGetByIdCardEndpoint",
                            summary = "Find a user by id card to the system",
                            tags = {"Find user by id card"},
                            security = @SecurityRequirement(name = "bearerAuth"),
                            parameters = {
                                    @Parameter(
                                            name = "idCard",
                                            in = ParameterIn.PATH,
                                            description = "ID card of the user to retrieve",
                                            required = true,
                                            example = "1234567890"
                                    )
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "User found successfully",
                                            content = @Content(
                                                    schema = @Schema(implementation = ResponseFindIdCardDto.class)
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
                .POST("/api/v1/login", handler::listenLogin)
                .POST("/api/v1/usuarios", handler::listenRegisterUser)
                .GET("/api/v1/usuarios/{idCard}", handler::listenGetByIdCardEndpoint)
                .build()
                .filter((request, next) ->
                        next.handle(request)
                                .onErrorResume(error -> {
                                    logger.error(error.getMessage());
                                    return globalErrorHandler.handleError(error);
                                })
                );
    }
}
