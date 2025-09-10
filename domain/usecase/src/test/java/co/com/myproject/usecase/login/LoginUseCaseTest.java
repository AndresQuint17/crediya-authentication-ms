package co.com.myproject.usecase.login;

import static org.junit.jupiter.api.Assertions.*;

import co.com.myproject.model.exceptions.RoleDoesNotExistException;
import co.com.myproject.model.role.Role;
import co.com.myproject.model.role.gateways.RoleRepository;
import co.com.myproject.model.user.User;
import co.com.myproject.model.user.gateways.UserRepository;
import co.com.myproject.usecase.exceptions.UserDoesNotExistException;
import co.com.myproject.usecase.login.dto.TokenDto;
import co.com.myproject.usecase.login.gateway.AuthRepository;
import co.com.myproject.usecase.validation.EValidationFieldMessages;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginUseCaseTest {

    @Mock private UserRepository userRepository;
    @Mock private RoleRepository roleRepository;
    @Mock private AuthRepository authRepository;

    @InjectMocks
    private LoginUseCase loginUseCase;

    @Test
    void shouldAuthenticateUserSuccessfully() {

        String email = "john@example.com";
        String password = "password123";
        String idCard = "123456789";
        Long roleId = 1L;
        String roleName = "USER";
        String roleDescription = "USER role";

        User user = new User("Joe", "Doe",
                idCard, null, "KR 154", "3215487954",
                "john@example.com", new BigDecimal(1442154), 1L);
        String credentials = "hashedPwd";
        Role role = new Role(roleName, roleDescription);
        TokenDto tokenDto = new TokenDto("jwt-token");

        when(userRepository.findByEmail(email)).thenReturn(Mono.just(user));
        when(userRepository.getUserRole(email)).thenReturn(Mono.just(roleId));
        when(roleRepository.getUserRoleNameByIdRol(roleId)).thenReturn(Mono.just(role));
        when(userRepository.getUserCredentials(idCard)).thenReturn(Mono.just(credentials));
        when(authRepository.login(idCard, password, credentials, roleName)).thenReturn(Mono.just(tokenDto));


        StepVerifier.create(loginUseCase.authenticate(email, password))
                .expectNext(tokenDto)
                .verifyComplete();
    }

    @Test
    void shouldReturnErrorWhenUserDoesNotExist() {
        String email = "missing@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Mono.empty());

        StepVerifier.create(loginUseCase.authenticate(email, "any"))
                .expectErrorSatisfies(error -> {
                    assertTrue(error instanceof UserDoesNotExistException);
                    assertEquals(EValidationFieldMessages.USER_DOES_NOT_EXIST.getMessage(), error.getMessage());
                })
                .verify();
    }

    @Test
    void shouldReturnErrorWhenUserHasNoRole() {
        String email = "john@example.com";
        String idCard = "123456789";

        User user = new User("Joe", "Doe",
                idCard, null, "KR 154", "3215487954",
                email, new BigDecimal(1442154), 1L);

        when(userRepository.findByEmail(email)).thenReturn(Mono.just(user));
        when(userRepository.getUserRole(email)).thenReturn(Mono.empty()); // Simula rol vacío

        StepVerifier.create(loginUseCase.authenticate(email, "pass"))
                .expectErrorSatisfies(error -> {
                    assertInstanceOf(RoleDoesNotExistException.class, error);
                    assertEquals(EValidationFieldMessages.ROLE_DOES_NOT_EXIST.getMessage(), error.getMessage());
                })
                .verify();
    }

    @Test
    void shouldReturnErrorWhenAuthFails() {

        String email = "john@example.com";
        String password = "wrongpass";
        String idCard = "123";
        Long roleId = 1L;
        String roleName = "USER";
        String roleDescription = "USER role";

        User user = new User("Joe", "Doe",
                idCard, null, "KR 154", "3215487954",
                "john@example.com", new BigDecimal(1442154), 1L);
        String credentials = "hashedPwd";
        Role role = new Role(roleName, roleDescription);

        when(userRepository.findByEmail(email)).thenReturn(Mono.just(user));
        when(userRepository.getUserRole(email)).thenReturn(Mono.just(roleId));
        when(roleRepository.getUserRoleNameByIdRol(roleId)).thenReturn(Mono.just(role));
        when(userRepository.getUserCredentials(idCard)).thenReturn(Mono.just(credentials));
        when(authRepository.login(idCard, password, credentials, roleName))
                .thenReturn(Mono.error(new RuntimeException("Authentication failed")));

        StepVerifier.create(loginUseCase.authenticate(email, password))
                .expectErrorMatches(error -> error instanceof RuntimeException
                        && error.getMessage().equals("Authentication failed"))
                .verify();
    }
}