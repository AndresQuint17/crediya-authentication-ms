package co.com.myproject.usecase.finduserbyid;

import co.com.myproject.model.user.User;
import co.com.myproject.model.user.gateways.UserRepository;
import co.com.myproject.usecase.exceptions.UserDoesNotExistException;
import co.com.myproject.usecase.validation.EValidationFieldMessages;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class FindUserByIdCardUseCase {
    private final UserRepository userRepository;

    public Mono<User> findUserByIdCard(String idCard){
        return userRepository.findByIdCard(idCard)
                .switchIfEmpty(Mono.error(new UserDoesNotExistException(
                        EValidationFieldMessages.USER_DOES_NOT_EXIST.getStatusCode(),
                        EValidationFieldMessages.USER_DOES_NOT_EXIST.getMessage())));
    }
}
