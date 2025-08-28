package co.com.myproject.api.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RegisterUserDtoTest {
    private Validator validator;

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private RegisterUserDto buildValidDto() {
        return new RegisterUserDto(
                "Andres",
                "Quintero",
                "1234567890",
                LocalDate.of(1994, 7, 17),
                "Siempre Viva Avenue",
                "3015484104",
                "andres@email.com",
                BigDecimal.valueOf(4_500_000)
        );
    }

    @Test
    void validDtoShouldPassValidation() {
        RegisterUserDto dto = buildValidDto();

        Set<ConstraintViolation<RegisterUserDto>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty(), "A valid DTO should not have validation errors");
    }

    @Test
    void firstNameCannotBeBlank() {
        RegisterUserDto dto = new RegisterUserDto(
                "   ",
                "Quintero",
                "1234567890",
                LocalDate.of(1994, 7, 17),
                "Siempre Viva Avenue",
                "3015484104",
                "andres@email.com",
                BigDecimal.valueOf(4_500_000)
        );

        Set<ConstraintViolation<RegisterUserDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("firstName")));
    }

    @Test
    void lastNameCannotBeBlank() {
        RegisterUserDto dto = new RegisterUserDto(
                "Andres",
                "",
                "1234567890",
                LocalDate.of(1994, 7, 17),
                "Siempre Viva Avenue",
                "3015484104",
                "andres@email.com",
                BigDecimal.valueOf(4_500_000)
        );

        Set<ConstraintViolation<RegisterUserDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("lastName")));
    }

    @Test
    void idCardCannotBeBlank() {
        RegisterUserDto dto = buildValidDto();
        dto = new RegisterUserDto(
                dto.firstName(),
                dto.lastName(),
                "   ",
                dto.dateOfBirth(),
                dto.address(),
                dto.phone(),
                dto.email(),
                dto.baseSalary()
        );

        Set<ConstraintViolation<RegisterUserDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("idCard")));
    }

    @Test
    void dateOfBirthMustBeInThePast() {
        RegisterUserDto dto = buildValidDto();
        dto = new RegisterUserDto(
                dto.firstName(),
                dto.lastName(),
                dto.idCard(),
                LocalDate.now().plusDays(1),
                dto.address(),
                dto.phone(),
                dto.email(),
                dto.baseSalary()
        );

        Set<ConstraintViolation<RegisterUserDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("dateOfBirth")));
    }

    @Test
    void emailMustBeValid() {
        RegisterUserDto dto = buildValidDto();
        dto = new RegisterUserDto(
                dto.firstName(),
                dto.lastName(),
                dto.idCard(),
                dto.dateOfBirth(),
                dto.address(),
                dto.phone(),
                "invalid-email",
                dto.baseSalary()
        );

        Set<ConstraintViolation<RegisterUserDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void baseSalaryMustBeGreaterThanOrEqualToZero() {
        RegisterUserDto dto = buildValidDto();
        dto = new RegisterUserDto(
                dto.firstName(),
                dto.lastName(),
                dto.idCard(),
                dto.dateOfBirth(),
                dto.address(),
                dto.phone(),
                dto.email(),
                BigDecimal.valueOf(-1000)
        );

        Set<ConstraintViolation<RegisterUserDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("baseSalary")));
    }

    @Test
    void baseSalaryMustBeLessThanOrEqualToLimit() {
        RegisterUserDto dto = buildValidDto();
        dto = new RegisterUserDto(
                dto.firstName(),
                dto.lastName(),
                dto.idCard(),
                dto.dateOfBirth(),
                dto.address(),
                dto.phone(),
                dto.email(),
                BigDecimal.valueOf(20_000_000)
        );

        Set<ConstraintViolation<RegisterUserDto>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("baseSalary")));
    }
}