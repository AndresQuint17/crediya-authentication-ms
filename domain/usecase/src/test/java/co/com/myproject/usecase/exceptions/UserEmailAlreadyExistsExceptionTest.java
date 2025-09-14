package co.com.myproject.usecase.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserEmailAlreadyExistsExceptionTest {
    @Test
    void testConstructorSetsFieldsCorrectly() {
        UserEmailAlreadyExistsException exception =
                new UserEmailAlreadyExistsException(409, "El email ya está registrado");

        assertEquals(409, exception.getStatus());
        assertEquals("El email ya está registrado", exception.getMessage());
    }
    @Test
    void testIsRuntimeException() {
        UserEmailAlreadyExistsException exception =
                new UserEmailAlreadyExistsException(400, "Bad request");

        assertInstanceOf(RuntimeException.class, exception);
    }
}