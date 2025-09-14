package co.com.myproject.usecase.validation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EValidationFieldMessagesTest {

    @Test
    @DisplayName("Should have correct message and status for USER_DOES_NOT_EXIST")
    void testUserDoesNotExistEnumValues() {
        EValidationFieldMessages error = EValidationFieldMessages.USER_DOES_NOT_EXIST;

        assertEquals("The user does not exist", error.getMessage());
        assertEquals(404, error.getStatusCode());
    }

    @Test
    @DisplayName("Should have correct message and status for EMAIL_INVALID")
    void testEmailInvalidEnumValues() {
        EValidationFieldMessages error = EValidationFieldMessages.EMAIL_INVALID;

        assertEquals("Email must be valid", error.getMessage());
        assertEquals(400, error.getStatusCode());
    }

    @Test
    @DisplayName("Should contain all expected enum values")
    void testEnumValuesCount() {
        int expectedCount = 13; // Update if you add more enums
        assertEquals(expectedCount, EValidationFieldMessages.values().length);
    }

    @Test
    @DisplayName("All enum values should have non-null message and valid status code")
    void testAllEnumValues() {
        for (EValidationFieldMessages error : EValidationFieldMessages.values()) {
            assertNotNull(error.getMessage(), "Message should not be null for " + error.name());
            assertTrue(error.getStatusCode() >= 100 && error.getStatusCode() <= 599,
                    "Status code should be a valid HTTP code for " + error.name());
        }
    }
}