package co.com.myproject.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Response object for find user by id card")
public record ResponseFindIdCardDto(
        @Schema(description = "User first name", example = "Andres David")
        String firstName,

        @Schema(description = "User last name", example = "Quintero Caicedo")
        String lastName,

        @Schema(description = "User identification document number", example = "1234567890")
        String idCard,

        @Schema(description = "User email", example = "andres.david.qc@email.com")
                String email,

        @Schema(description = "User base salary", example = "4500000.00")
        BigDecimal baseSalary
) {
}
