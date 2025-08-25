package co.com.myproject.api.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UserAlreadyExistsException extends RuntimeException {
    private final HttpStatus status;
    public UserAlreadyExistsException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
}
