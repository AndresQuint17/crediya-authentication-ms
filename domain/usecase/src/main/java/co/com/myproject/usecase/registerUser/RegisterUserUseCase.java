package co.com.myproject.usecase.registerUser;

import co.com.myproject.model.user.User;
import co.com.myproject.model.user.gateways.UserRepository;
import co.com.myproject.usecase.exceptions.UserEmailAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RegisterUserUseCase {
    private final UserRepository userRepository;

    public Mono<User> registerUser(User user){
        return userRepository.findByEmail(user.getEmail())
                .flatMap(existingUser ->
                        Mono.<User>error(new UserEmailAlreadyExistsException(400, "Email is already registered: " + user.getEmail()))
                )
                .switchIfEmpty(userRepository.registerUser(user));
    }
}
