package co.com.myproject.usecase.login;

import co.com.myproject.model.role.gateways.RoleRepository;
import co.com.myproject.model.user.gateways.UserRepository;
import co.com.myproject.model.exceptions.RoleDoesNotExistException;
import co.com.myproject.usecase.exceptions.UserDoesNotExistException;
import co.com.myproject.usecase.login.dto.TokenDto;
import co.com.myproject.usecase.login.gateway.AuthRepository;
import co.com.myproject.usecase.validation.EValidationFieldMessages;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class LoginUseCase {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthRepository authRepository;

    public Mono<TokenDto> authenticate(String email, String password) {
        return userRepository.findByEmail(email)
                .switchIfEmpty(Mono.error(new UserDoesNotExistException(
                        EValidationFieldMessages.USER_DOES_NOT_EXIST.getStatusCode(),
                        EValidationFieldMessages.USER_DOES_NOT_EXIST.getMessage()
                )))
                .flatMap(user -> userRepository.getUserRole(user.getEmail())
                        .switchIfEmpty(Mono.error(new RoleDoesNotExistException(
                                EValidationFieldMessages.ROLE_DOES_NOT_EXIST.getStatusCode(),
                                EValidationFieldMessages.ROLE_DOES_NOT_EXIST.getMessage())))
                        .flatMap(roleId -> roleRepository.getUserRoleNameByIdRol(roleId)
                                .flatMap(role -> userRepository.getUserCredentials(user.getIdCard())
                                        .flatMap(userCredentials -> authRepository.login(
                                                user.getIdCard(),
                                                password,
                                                userCredentials,
                                                role.getName()
                                        ))
                                )
                        )
                );
    }
}
