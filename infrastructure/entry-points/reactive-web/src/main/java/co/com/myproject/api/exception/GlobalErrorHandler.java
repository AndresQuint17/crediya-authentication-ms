package co.com.myproject.api.exception;

import co.com.myproject.model.exceptions.RoleDoesNotExistException;
import co.com.myproject.usecase.exceptions.UserDoesNotExistException;
import co.com.myproject.usecase.exceptions.UserEmailAlreadyExistsException;
import org.springframework.core.codec.DecodingException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class GlobalErrorHandler {

    public Mono<ServerResponse> handleError(Throwable throwable) {
        final String BUSINESS_VALIDATION_ERROR = "Business validation error";
        final String APPLICATION_ERROR = "Application error";
        final String SERVER_ERROR = "Internal server error";

        //Bad credentials to generate token
        if (throwable instanceof BadCredentialsException) {
            return ServerResponse.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(Map.of(
                            "error", BUSINESS_VALIDATION_ERROR,
                            "message", throwable.getMessage()
                    ));
        }
        //User already exists exception
        if (throwable instanceof UserEmailAlreadyExistsException) {
            return ServerResponse.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(Map.of(
                            "error", BUSINESS_VALIDATION_ERROR,
                            "status", ((UserEmailAlreadyExistsException) throwable).getStatus(),
                            "message", throwable.getMessage()
                    ));
        }
        // User does not exist
        if (throwable instanceof UserDoesNotExistException) {
            return ServerResponse.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(Map.of(
                            "error", BUSINESS_VALIDATION_ERROR,
                            "status", ((UserDoesNotExistException) throwable).getStatus(),
                            "message", throwable.getMessage()
                    ));
        }
        // User role does not exist
        if(throwable instanceof RoleDoesNotExistException) {
            return ServerResponse.status(HttpStatus.NOT_FOUND)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(Map.of(
                            "error", BUSINESS_VALIDATION_ERROR,
                            "status", ((RoleDoesNotExistException) throwable).getStatus(),
                            "message", throwable.getMessage()
                    ));
        }
        // Duplicate key exception
        if(throwable instanceof DuplicateKeyException) {
            return ServerResponse.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(Map.of(
                            "error", BUSINESS_VALIDATION_ERROR,
                            "status", 400,
                            "message", "Id card is already registered"
                    ));
        }
        // Format date exception
        if (throwable instanceof ServerWebInputException) {
            Throwable cause = throwable.getCause();
            if (cause instanceof DecodingException) {
                return ServerResponse.status(HttpStatus.BAD_REQUEST)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(Map.of(
                                "error", BUSINESS_VALIDATION_ERROR,
                                "status", 400,
                                "message", "Invalid date format. Expected format: yyyy-MM-dd"
                        ));
            }
            return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(Map.of(
                            "error", SERVER_ERROR,
                            "status",500,
                            "message", "Unexpected server error occurred"
                    ));
        }
        // Authorization denied access
        if(throwable instanceof AuthorizationDeniedException) {
            return ServerResponse.status(HttpStatus.UNAUTHORIZED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(Map.of(
                            "error", BUSINESS_VALIDATION_ERROR,
                            "message", "You do not have authorization to use this resource"
                    ));
        }
        // Data source connection failed
        if(throwable instanceof DataAccessResourceFailureException) {
            return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(Map.of(
                            "error", APPLICATION_ERROR,
                            "status", 500,
                            "message", "Error connecting to data source"
                    ));
        }
        // Generic exception
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of(
                        "error", SERVER_ERROR,
                        "status",500,
                        "message", "Unexpected server error occurred"
                ));
    }
}
