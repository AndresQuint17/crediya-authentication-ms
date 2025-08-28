package co.com.myproject.r2dbc.entity;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserEntityTest {
    @Test
    void mustCreateUserWithBuilder() {
        UserEntity user = UserEntity.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .idCard("123456")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .address("Main St 123")
                .phone("3001234567")
                .email("john@test.com")
                .baseSalary(BigDecimal.valueOf(2000))
                .roleId(2L)
                .build();

        assertAll(
                () -> assertEquals(1L, user.getId()),
                () -> assertEquals("John", user.getFirstName()),
                () -> assertEquals("Doe", user.getLastName()),
                () -> assertEquals("123456", user.getIdCard()),
                () -> assertEquals(LocalDate.of(1990, 1, 1), user.getDateOfBirth()),
                () -> assertEquals("Main St 123", user.getAddress()),
                () -> assertEquals("3001234567", user.getPhone()),
                () -> assertEquals("john@test.com", user.getEmail()),
                () -> assertEquals(BigDecimal.valueOf(2000), user.getBaseSalary()),
                () -> assertEquals(2L, user.getRoleId())
        );
    }

    @Test
    void mustCreateUserWithAllArgsConstructor() {
        UserEntity user = new UserEntity(
                1L,
                "John",
                "Doe",
                "123456",
                LocalDate.of(1990, 1, 1),
                "Main St 123",
                "3001234567",
                "john@test.com",
                BigDecimal.valueOf(2000),
                2L
        );

        assertEquals("John", user.getFirstName());
        assertEquals("john@test.com", user.getEmail());
    }

    @Test
    void mustCreateUserWithNoArgsConstructorAndSetters() {
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setFirstName("Jane");
        user.setLastName("Smith");
        user.setEmail("jane@test.com");

        assertEquals(1L, user.getId());
        assertEquals("Jane", user.getFirstName());
        assertEquals("Smith", user.getLastName());
        assertEquals("jane@test.com", user.getEmail());
    }

    @Test
    void mustCreateNewUserFromToBuilder() {
        UserEntity user = UserEntity.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john@test.com")
                .build();

        UserEntity updated = user.toBuilder()
                .lastName("Smith")
                .build();

        assertEquals("Smith", updated.getLastName());
        assertEquals("john@test.com", updated.getEmail());
        assertNotSame(user, updated); // asegura que no es el mismo objeto
    }

    @Test
    void mustAllowNullValues() {
        UserEntity user = UserEntity.builder()
                .id(null)
                .firstName(null)
                .lastName(null)
                .idCard(null)
                .dateOfBirth(null)
                .address(null)
                .phone(null)
                .email(null)
                .baseSalary(null)
                .roleId(null)
                .build();

        assertNull(user.getFirstName());
        assertNull(user.getEmail());
    }
}