package co.com.myproject.usecase.finduserbyid;

import co.com.myproject.model.user.User;
import co.com.myproject.model.user.gateways.UserRepository;
import co.com.myproject.usecase.exceptions.UserDoesNotExistException;
import co.com.myproject.usecase.validation.EValidationFieldMessages;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FindUserByIdCardUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FindUserByIdCardUseCase findUserByIdCardUseCase;

    @Test
    void shouldReturnUser_whenUserExists() {

        String idCard = "123456789";
        User expectedUser = new User("Joe", "Doe",
                idCard, null, "KR 154", "3215487954",
                "joedoe@email.com", new BigDecimal(1442154), 1L);

        Mockito.when(userRepository.findByIdCard(idCard))
                .thenReturn(Mono.just(expectedUser));


        StepVerifier.create(findUserByIdCardUseCase.findUserByIdCard(idCard))
                .expectNext(expectedUser)
                .verifyComplete();
    }

    @Test
    void shouldThrowUserDoesNotExistException_whenUserNotFound() {

        String idCard = "987654321";
        Mockito.when(userRepository.findByIdCard(idCard))
                .thenReturn(Mono.empty());


        StepVerifier.create(findUserByIdCardUseCase.findUserByIdCard(idCard))
                .expectErrorSatisfies(error -> {
                    assertInstanceOf(UserDoesNotExistException.class, error);
                    UserDoesNotExistException ex = (UserDoesNotExistException) error;
                    assertEquals(EValidationFieldMessages.USER_DOES_NOT_EXIST.getStatusCode(), ex.getStatus());
                    assertEquals(EValidationFieldMessages.USER_DOES_NOT_EXIST.getMessage(), ex.getMessage());
                })
                .verify();
    }
}