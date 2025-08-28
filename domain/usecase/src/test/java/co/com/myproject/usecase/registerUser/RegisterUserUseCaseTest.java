package co.com.myproject.usecase.registerUser;

import co.com.myproject.model.user.User;
import co.com.myproject.model.user.gateways.UserRepository;
import co.com.myproject.usecase.exceptions.UserEmailAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class RegisterUserUseCaseTest {
    private UserRepository userRepository;
    private RegisterUserUseCase registerUserUseCase;

    @BeforeEach
    void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        registerUserUseCase = new RegisterUserUseCase(userRepository);
    }

    private User buildSampleUser() {
        return User.builder()
                .firstName("Carlos")
                .lastName("Ramírez")
                .idCard("112233")
                .dateOfBirth(LocalDate.of(1995, 1, 1))
                .address("Calle 45 #10")
                .phone("3001234567")
                .email("carlos@example.com")
                .baseSalary(BigDecimal.valueOf(7000))
                .build();
    }

    @Test
    void whenEmailAlreadyExists_thenThrowsException() {
        User user = buildSampleUser();

        // Mock: si busca el email, encuentra un usuario existente
        when(userRepository.findByEmail(eq(user.getEmail())))
                .thenReturn(Mono.just(user));

        // Mockear también registerUser (aunque no debe usarse) para evitar null en switchIfEmpty
        when(userRepository.registerUser(any(User.class)))
                .thenReturn(Mono.just(user));

        StepVerifier.create(registerUserUseCase.registerUser(user))
                .expectErrorSatisfies(error -> {
                    assertTrue(error instanceof UserEmailAlreadyExistsException);
                    assertTrue(error.getMessage().contains(user.getEmail()));
                })
                .verify();

        verify(userRepository, times(1)).findByEmail(user.getEmail());
        verify(userRepository, never()).registerUser(any(User.class));
    }

    @Test
    void whenEmailDoesNotExist_thenRegistersUser() {
        User user = buildSampleUser();

        // Mock: si busca el email, no encuentra nada
        when(userRepository.findByEmail(eq(user.getEmail())))
                .thenReturn(Mono.empty());

        // Mock: simula guardar y devolver el usuario
        when(userRepository.registerUser(any(User.class)))
                .thenReturn(Mono.just(user));

        StepVerifier.create(registerUserUseCase.registerUser(user))
                .expectNextMatches(savedUser ->
                        savedUser.getEmail().equals(user.getEmail()) &&
                                savedUser.getFirstName().equals("Carlos"))
                .verifyComplete();

        verify(userRepository, times(1)).findByEmail(user.getEmail());
        verify(userRepository, times(1)).registerUser(user);
    }
}