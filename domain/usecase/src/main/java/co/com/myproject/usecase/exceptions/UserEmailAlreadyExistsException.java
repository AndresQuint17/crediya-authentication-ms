package co.com.myproject.usecase.exceptions;

import lombok.Getter;

@Getter
public class UserEmailAlreadyExistsException extends RuntimeException {
    private final int status;
    public UserEmailAlreadyExistsException(int status, String message) {
        super(message);
        this.status = status;
    }
}
