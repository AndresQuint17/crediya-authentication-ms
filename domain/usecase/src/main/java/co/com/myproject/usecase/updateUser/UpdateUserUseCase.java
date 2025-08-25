package co.com.myproject.usecase.updateUser;

import co.com.myproject.model.user.User;
import co.com.myproject.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UpdateUserUseCase {
    private final UserRepository userRepository;

    public Mono<User> updateUser(User user){
        return userRepository.updateUser(user);
    }
}
