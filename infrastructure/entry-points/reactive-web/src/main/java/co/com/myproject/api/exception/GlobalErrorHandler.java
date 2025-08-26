package co.com.myproject.api.exception;

import co.com.myproject.usecase.exceptions.UserEmailAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class GlobalErrorHandler {
    public Mono<ServerResponse> handleError(Throwable throwable, ServerRequest request) {
        //User already exists exception
        if (throwable instanceof UserEmailAlreadyExistsException) {
            return ServerResponse.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(Map.of(
                            "error", "Business error",
                            "status", ((UserEmailAlreadyExistsException) throwable).getStatus(),
                            "message", throwable.getMessage()
                    ));
        }
        // Generic exception
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of(
                        "error", "Internal server error",
                        "status",500,
                        "message", throwable.getMessage()
                ));
    }
}
