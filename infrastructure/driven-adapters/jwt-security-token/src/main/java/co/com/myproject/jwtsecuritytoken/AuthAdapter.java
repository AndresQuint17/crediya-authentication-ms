package co.com.myproject.jwtsecuritytoken;

import co.com.myproject.api.exception.BadCredentialsException;
import co.com.myproject.api.jwt.AuthDetails;
import co.com.myproject.api.jwt.provider.JwtTokenProvider;
import co.com.myproject.usecase.login.dto.TokenDto;
import co.com.myproject.usecase.login.gateway.AuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthAdapter implements AuthRepository {
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtProvider;

    @Override
    public Mono<TokenDto> login(String username, String passwordDto, String passwordStored, String roles) {
        if (passwordEncoder.matches(passwordDto, passwordStored)) {
            AuthDetails authDetails = new AuthDetails(username, passwordDto, true, roles);
            return Mono.just(new TokenDto(jwtProvider.generateToken(authDetails)));
        } else {
            return Mono.error(new BadCredentialsException("Bad credentials"));
        }
    }
}
