package co.com.myproject.usecase.exceptions;

import lombok.Getter;

@Getter
public class UserRoleNotFoundException extends RuntimeException {
    public UserRoleNotFoundException(String message) {
        super(message);
    }
}
