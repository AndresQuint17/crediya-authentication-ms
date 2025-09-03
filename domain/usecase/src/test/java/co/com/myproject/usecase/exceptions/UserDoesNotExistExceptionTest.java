package co.com.myproject.usecase.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserDoesNotExistExceptionTest {
    @Test
    void testConstructorSetsFieldsCorrectly() {
        UserDoesNotExistException exception =
                new UserDoesNotExistException(409, "User already registered");

        assertEquals(409, exception.getStatus());
        assertEquals("User already registered", exception.getMessage());
    }
    @Test
    void testIsRuntimeException() {
        UserDoesNotExistException exception =
                new UserDoesNotExistException(400, "Bad request");

        assertInstanceOf(RuntimeException.class, exception);
    }
}