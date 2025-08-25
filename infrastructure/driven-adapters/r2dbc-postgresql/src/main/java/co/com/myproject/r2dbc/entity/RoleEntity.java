package co.com.myproject.r2dbc.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("roles")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
public class RoleEntity {
    @Id
    @Column("role_id")
    private Long id;

    @Column("name")
    private String name;

    @Column("description")
    private String description;
}
