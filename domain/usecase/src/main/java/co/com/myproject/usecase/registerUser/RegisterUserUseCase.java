package co.com.myproject.usecase.registerUser;

import co.com.myproject.model.role.gateways.RoleRepository;
import co.com.myproject.model.user.User;
import co.com.myproject.model.user.gateways.UserRepository;
import co.com.myproject.usecase.exceptions.UserEmailAlreadyExistsException;
import co.com.myproject.usecase.exceptions.UserRoleNotFoundException;
import co.com.myproject.usecase.validation.EValidationFieldMessages;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RegisterUserUseCase {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public Mono<User> registerUser(User user) {
        return userRepository.findByEmail(user.getEmail())
                .flatMap(existingUser ->
                        Mono.<User>error(new UserEmailAlreadyExistsException(
                                EValidationFieldMessages.EMAIL_ALREADY_REGISTERED.getStatusCode(),
                                EValidationFieldMessages.EMAIL_ALREADY_REGISTERED.getMessage() + user.getEmail()))
                )
                .switchIfEmpty(Mono.defer(() -> roleRepository.isRoleExisting(user.getRoleId())
                        .flatMap(roleExists -> {
                            if (!roleExists) {
                                return Mono.error(new UserRoleNotFoundException(
                                        EValidationFieldMessages.ROLE_NOT_FOUND.getMessage()));
                            }
                            return userRepository.registerUser(user); // Si el rol existe, se registra el usuario
                        })
                ));
    }
}
