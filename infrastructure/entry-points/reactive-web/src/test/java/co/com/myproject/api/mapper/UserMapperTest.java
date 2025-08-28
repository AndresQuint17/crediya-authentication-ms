package co.com.myproject.api.mapper;

import co.com.myproject.api.dto.RegisterUserDto;
import co.com.myproject.model.user.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = UserMapperImpl.class)
class UserMapperTest {
    @Autowired
    private UserMapper mapper;

    @Test
    void shouldMapDtoToModel() {
        RegisterUserDto dto = new RegisterUserDto(
                "Andres David",
                "Quintero Caicedo",
                "1234567890",
                LocalDate.of(1994, 7, 17),
                "Siempre Viva Avenue",
                "+57 3015484104",
                "andres.david.qc@email.com",
                new BigDecimal("4500000.00")
        );

        User user = mapper.toModel(dto);

        assertNotNull(user);
        assertEquals("Andres David", user.getFirstName());
        assertEquals("Quintero Caicedo", user.getLastName());
        assertEquals("1234567890", user.getIdCard());
        assertEquals(LocalDate.of(1994, 7, 17), user.getDateOfBirth());
        assertEquals("Siempre Viva Avenue", user.getAddress());
        assertEquals("+57 3015484104", user.getPhone());
        assertEquals("andres.david.qc@email.com", user.getEmail());
        assertEquals(new BigDecimal("4500000.00"), user.getBaseSalary());
    }

    @Test
    void shouldMapModelToDto() {
        User user = User.builder()
                .firstName("Andres David")
                .lastName("Quintero Caicedo")
                .idCard("1234567890")
                .dateOfBirth(LocalDate.of(1994, 7, 17))
                .address("Siempre Viva Avenue")
                .phone("+57 3015484104")
                .email("andres.david.qc@email.com")
                .baseSalary(new BigDecimal("4500000.00"))
                .build();

        RegisterUserDto dto = mapper.toDto(user);

        assertNotNull(dto);
        assertEquals("Andres David", dto.firstName());
        assertEquals("Quintero Caicedo", dto.lastName());
        assertEquals("1234567890", dto.idCard());
        assertEquals(LocalDate.of(1994, 7, 17), dto.dateOfBirth());
        assertEquals("Siempre Viva Avenue", dto.address());
        assertEquals("+57 3015484104", dto.phone());
        assertEquals("andres.david.qc@email.com", dto.email());
        assertEquals(new BigDecimal("4500000.00"), dto.baseSalary());
    }
}