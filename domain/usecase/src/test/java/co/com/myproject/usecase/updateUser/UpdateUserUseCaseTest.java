package co.com.myproject.usecase.updateUser;

import co.com.myproject.model.user.User;
import co.com.myproject.model.user.gateways.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.*;

class UpdateUserUseCaseTest {

    private UserRepository userRepository;
    private UpdateUserUseCase updateUserUseCase;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        updateUserUseCase = new UpdateUserUseCase(userRepository);
    }

    @Test
    void shouldUpdateUser() {
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

        when(userRepository.updateUser(user)).thenReturn(Mono.just(user));

        StepVerifier.create(updateUserUseCase.updateUser(user))
                .expectNext(user)
                .verifyComplete();

        verify(userRepository, times(1)).updateUser(user);
    }
}