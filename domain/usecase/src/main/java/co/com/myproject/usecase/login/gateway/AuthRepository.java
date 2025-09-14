package co.com.myproject.usecase.login.gateway;

import co.com.myproject.usecase.login.dto.TokenDto;
import reactor.core.publisher.Mono;

public interface AuthRepository {
    Mono<TokenDto> login(String username, String passwordDto, String passwordStored, String roles);
}
