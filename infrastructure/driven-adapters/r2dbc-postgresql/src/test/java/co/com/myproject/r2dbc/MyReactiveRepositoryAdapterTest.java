package co.com.myproject.r2dbc;

import co.com.myproject.model.user.User;
import co.com.myproject.r2dbc.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.reactivecommons.utils.ObjectMapper;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MyReactiveRepositoryAdapterTest {

    @InjectMocks
    UserReactiveRepositoryAdapter repositoryAdapter;

    @Mock
    UserReactiveRepository repository;

    @Mock
    ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        // El adapter necesita el mapper y el repo real en el constructor
        repositoryAdapter = new UserReactiveRepositoryAdapter(repository, mapper);
    }

    private UserEntity buildEntity() {
        return UserEntity.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .idCard("123456")
                .dateOfBirth(LocalDate.of(1990, 1, 1))
                .address("Main St 123")
                .phone("3001234567")
                .email("john@test.com")
                .baseSalary(BigDecimal.valueOf(2000))
                .roleId(2L)
                .build();
    }

    private User buildDomain() {
        return new User(
                "John",
                "Doe",
                "123456",
                LocalDate.of(1990, 1, 1),
                "Main St 123",
                "3001234567",
                "john@test.com",
                BigDecimal.valueOf(2000), 1L
        );
    }

    @Test
    void mustRegisterUserAndMapAllFields() {
        UserEntity entity = buildEntity();
        User model = buildDomain();

        // Mock: mapper transforma User -> UserEntity
        when(mapper.map(model, UserEntity.class)).thenReturn(entity);

        // Mock: repo guarda la entidad y devuelve la misma
        when(repository.save(entity)).thenReturn(Mono.just(entity));

        // Mock: mapper transforma UserEntity -> User
        when(mapper.map(entity, User.class)).thenReturn(model);

        Mono<User> result = repositoryAdapter.registerUser(model);

        StepVerifier.create(result)
                .assertNext(saved -> {
                    assertEquals("John", saved.getFirstName());
                    assertEquals("Doe", saved.getLastName());
                    assertEquals("123456", saved.getIdCard());
                    assertEquals(LocalDate.of(1990, 1, 1), saved.getDateOfBirth());
                    assertEquals("Main St 123", saved.getAddress());
                    assertEquals("3001234567", saved.getPhone());
                    assertEquals("john@test.com", saved.getEmail());
                    assertEquals(BigDecimal.valueOf(2000), saved.getBaseSalary());
                })
                .verifyComplete();
    }

    @Test
    void mustFindByEmail() {
        User user = User.builder().email("test@email.com").build();
        UserEntity entity = UserEntity.builder().id(1L).email("test@email.com").build();

        when(repository.findByEmail("test@email.com")).thenReturn(Mono.just(entity));
        when(mapper.map(entity, User.class)).thenReturn(user);

        Mono<User> result = repositoryAdapter.findByEmail("test@email.com");

        StepVerifier.create(result)
                .expectNextMatches(found -> found.getEmail().equals("test@email.com"))
                .verifyComplete();
    }

    @Test
    void updateUserMustReturnNull() {
        User user = User.builder().email("test@email.com").build();

        Mono<User> result = repositoryAdapter.updateUser(user);

        assertNull(result);
    }
}
