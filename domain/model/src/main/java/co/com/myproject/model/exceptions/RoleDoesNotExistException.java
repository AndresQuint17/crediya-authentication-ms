package co.com.myproject.model.exceptions;

import lombok.Getter;

@Getter
public class RoleDoesNotExistException extends RuntimeException {
    private final int status;
    public RoleDoesNotExistException(int status, String message) {
        super(message);
        this.status = status;
    }
}
