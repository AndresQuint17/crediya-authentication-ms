package co.com.myproject.r2dbc.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Table("users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
public class UserEntity {
    @Id
    @Column("user_id")
    private Long id;

    @Column("first_name")
    private String firstName;

    @Column("last_name")
    private String lastName;

    @Column("id_card")
    private String idCard;

    @Column("date_of_birth")
    private LocalDate dateOfBirth;

    @Column("address")
    private String address;

    @Column("phone")
    private String phone;

    @Column("email")
    private String email;

    @Column("base_salary")
    private BigDecimal baseSalary;

    @Column("role_id")
    private Long roleId;

    @Column("password")
    private String password;
}
