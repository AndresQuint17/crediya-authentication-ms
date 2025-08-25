package co.com.myproject.api.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RegisterUserDto(
        @NotBlank(message = "First name cannot be null or empty")
        String firstName,

        @NotBlank(message = "Last name cannot be null or empty")
        String lastName,

        @NotBlank(message = "ID card cannot be null or empty")
        String idCard,

        @Past(message = "Date of birth must be in the past")
        LocalDate dateOfBirth,

        String address,

        //@Pattern(regexp = "\\d{10,15}", message = "Phone must contain between 10 and 15 digits")
        String phone,

        @NotBlank(message = "Email cannot be null or empty")
        @Email(message = "Email must be valid")
        String email,

        @NotNull(message = "Base salary cannot be null")
        @DecimalMin(value = "0.0", inclusive = true, message = "Base salary must be greater than or equal to 0")
        @DecimalMax(value = "15000000.0", message = "Base salary must be less than or equal to 15000000")
        BigDecimal baseSalary
) {
}
