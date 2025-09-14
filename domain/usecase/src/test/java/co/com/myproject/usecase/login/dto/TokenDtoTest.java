package co.com.myproject.usecase.login.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TokenDtoTest {
    @Test
    void shouldStoreTokenValue() {
        String tokenValue = "jwt-token-123";
        TokenDto tokenDto = new TokenDto(tokenValue);

        assertEquals(tokenValue, tokenDto.token());
    }

    @Test
    void shouldBeEqualIfTokensAreSame() {
        TokenDto token1 = new TokenDto("abc123");
        TokenDto token2 = new TokenDto("abc123");

        assertEquals(token1, token2);
        assertEquals(token1.hashCode(), token2.hashCode());
    }

    @Test
    void shouldNotBeEqualIfTokensAreDifferent() {
        TokenDto token1 = new TokenDto("abc123");
        TokenDto token2 = new TokenDto("xyz789");

        assertNotEquals(token1, token2);
    }

    @Test
    void shouldHaveReadableToString() {
        TokenDto tokenDto = new TokenDto("abc123");

        assertEquals("TokenDto[token=abc123]", tokenDto.toString());
    }
}