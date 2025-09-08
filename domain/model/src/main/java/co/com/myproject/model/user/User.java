package co.com.myproject.model.user;

import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private String firstName;
    private String lastName;
    private String idCard;
    private LocalDate dateOfBirth;
    private String address;
    private String phone;
    private String email;
    private BigDecimal baseSalary;
    private Long roleId;
}
