package co.com.myproject.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Request object for register new user")
public record RegisterUserDto(
        @Schema(description = "User first name", example = "Andres David")
        @NotBlank(message = "First name cannot be null or empty")
        String firstName,

        @Schema(description = "User last name", example = "Quintero Caicedo")
        @NotBlank(message = "Last name cannot be null or empty")
        String lastName,

        @Schema(description = "User identification document number", example = "1234567890")
        @NotBlank(message = "ID card cannot be null or empty")
        String idCard,

        @Schema(description = "User date of birth", example = "1994-07-17")
        @Past(message = "Date of birth must be in the past")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate dateOfBirth,

        @Schema(description = "User address", example = "Siempre Viva Avenue")
        String address,

        //@Pattern(regexp = "\\d{10,15}", message = "Phone must contain between 10 and 15 digits")
        @Schema(description = "User phone number", example = "+57 3015484104")
        String phone,

        @Schema(description = "User email", example = "andres.david.qc@email.com")
        @NotBlank(message = "Email cannot be null or empty")
        @Email(message = "Email must be valid")
        String email,

        @Schema(description = "User base salary", example = "4500000.00")
        @NotNull(message = "Base salary cannot be null")
        @DecimalMin(value = "0.0", inclusive = true, message = "Base salary must be greater than or equal to 0")
        @DecimalMax(value = "15000000.0", message = "Base salary must be less than or equal to 15000000")
        BigDecimal baseSalary,

        @Schema(description = "User role", example = "3")
        @Positive(message = "Role id must be a positive number")
        @NotNull(message = "Role id cannot be null")
        Long roleId
) {
}
