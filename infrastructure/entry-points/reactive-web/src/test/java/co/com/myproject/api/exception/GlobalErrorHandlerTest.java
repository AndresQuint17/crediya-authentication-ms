package co.com.myproject.api.exception;

import co.com.myproject.usecase.exceptions.UserEmailAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import reactor.test.StepVerifier;

import java.util.Objects;

class GlobalErrorHandlerTest {
    private GlobalErrorHandler errorHandler;
    //private ServerRequest mockRequest;

    @BeforeEach
    void setUp() {
        errorHandler = new GlobalErrorHandler();
        //mockRequest = mock(ServerRequest.class);
    }

    @Test
    void handleError_ShouldReturnBadRequest_WhenUserEmailAlreadyExistsException() {
        // given
        UserEmailAlreadyExistsException ex = new UserEmailAlreadyExistsException(400, "Email already exists");

        // when
        var responseMono = errorHandler.handleError(ex);

        // then
        StepVerifier.create(responseMono)
                .assertNext(response -> {
                    assert response.statusCode().equals(HttpStatus.BAD_REQUEST);
                    assert response.headers().getContentType().equals(MediaType.APPLICATION_JSON);
                })
                .expectComplete()
                .verify();
    }

    @Test
    void handleError_ShouldReturnInternalServerError_WhenDataAccessResourceFailureException() {
        // given
        DataAccessResourceFailureException ex =
                new DataAccessResourceFailureException("DB not reachable");

        // when
        var responseMono = errorHandler.handleError(ex);

        // then
        StepVerifier.create(responseMono)
                .assertNext(response -> {
                    assert response.statusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR);
                    assert Objects.equals(response.headers().getContentType(), MediaType.APPLICATION_JSON);
                })
                .expectComplete()
                .verify();
    }

    @Test
    void handleError_ShouldReturnInternalServerError_WhenGenericException() {
        // given
        RuntimeException ex = new RuntimeException("Some unexpected error");

        // when
        var responseMono = errorHandler.handleError(ex);

        // then
        StepVerifier.create(responseMono)
                .assertNext(response -> {
                    assert response.statusCode().equals(HttpStatus.INTERNAL_SERVER_ERROR);
                    assert Objects.equals(response.headers().getContentType(), MediaType.APPLICATION_JSON);
                })
                .expectComplete()
                .verify();
    }
}