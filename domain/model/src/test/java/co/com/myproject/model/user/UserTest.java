package co.com.myproject.model.user;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testNoArgsConstructorAndSetters() {
        User user = new User();
        user.setFirstName("Juan");
        user.setLastName("Pérez");
        user.setIdCard("123456");
        user.setDateOfBirth(LocalDate.of(1990, 5, 20));
        user.setAddress("Calle Falsa 123");
        user.setPhone("3001234567");
        user.setEmail("juan@example.com");
        user.setBaseSalary(BigDecimal.valueOf(5000));

        assertEquals("Juan", user.getFirstName());
        assertEquals("Pérez", user.getLastName());
        assertEquals("123456", user.getIdCard());
        assertEquals(LocalDate.of(1990, 5, 20), user.getDateOfBirth());
        assertEquals("Calle Falsa 123", user.getAddress());
        assertEquals("3001234567", user.getPhone());
        assertEquals("juan@example.com", user.getEmail());
        assertEquals(BigDecimal.valueOf(5000), user.getBaseSalary());
    }

    @Test
    void testAllArgsConstructor() {
        User user = new User(
                "Ana",
                "Gómez",
                "654321",
                LocalDate.of(1985, 10, 15),
                "Av Siempre Viva 742",
                "3109876543",
                "ana@example.com",
                BigDecimal.valueOf(8000)
        );

        assertEquals("Ana", user.getFirstName());
        assertEquals("Gómez", user.getLastName());
        assertEquals("654321", user.getIdCard());
        assertEquals(LocalDate.of(1985, 10, 15), user.getDateOfBirth());
        assertEquals("Av Siempre Viva 742", user.getAddress());
        assertEquals("3109876543", user.getPhone());
        assertEquals("ana@example.com", user.getEmail());
        assertEquals(BigDecimal.valueOf(8000), user.getBaseSalary());
    }

    @Test
    void testBuilder() {
        User user = User.builder()
                .firstName("Luis")
                .lastName("Martínez")
                .idCard("999999")
                .dateOfBirth(LocalDate.of(2000, 1, 1))
                .address("Carrera 45 #10")
                .phone("3015551234")
                .email("luis@example.com")
                .baseSalary(BigDecimal.valueOf(10000))
                .build();

        assertNotNull(user);
        assertEquals("Luis", user.getFirstName());
        assertEquals("Martínez", user.getLastName());
        assertEquals("999999", user.getIdCard());
        assertEquals(LocalDate.of(2000, 1, 1), user.getDateOfBirth());
        assertEquals("Carrera 45 #10", user.getAddress());
        assertEquals("3015551234", user.getPhone());
        assertEquals("luis@example.com", user.getEmail());
        assertEquals(BigDecimal.valueOf(10000), user.getBaseSalary());
    }

}