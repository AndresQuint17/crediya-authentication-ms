package co.com.myproject.model.user.gateways;

import co.com.myproject.model.user.User;
import reactor.core.publisher.Mono;

public interface UserRepository {
    Mono<User> registerUser(User user);
    Mono<User> updateUser(User user);
    Mono<User> findByEmail(String email);
    Mono<User> findByIdCard(String idCard);
}
