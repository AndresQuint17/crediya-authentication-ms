package co.com.myproject.api.exception;

import lombok.Getter;

@Getter
public class BadCredentialsException extends RuntimeException {
    public BadCredentialsException(String message) {
        super(message);
    }
}
