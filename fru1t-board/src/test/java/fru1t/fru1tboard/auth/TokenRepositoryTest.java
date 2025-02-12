package fru1t.fru1tboard.auth;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TokenRepositoryTest {
    @Autowired
    private TokenRepository tokenRepository;

    @Test
    void saveRefreshTokenTest(){
        // given
        String username = "testUser";
        String refreshToken = "refreshToken";

        // when
        tokenRepository.saveRefreshToken(username, refreshToken);

        // then
        String result = tokenRepository.getRefreshToken(username);
        assertThat(result).isEqualTo(refreshToken);
    }
    @Test
    void deleteRefreshTokenTest(){
        // given
        String username = "testUser";
        String refreshToken = "refreshToken";
        tokenRepository.saveRefreshToken(username, refreshToken);
        // when

        tokenRepository.deleteRefreshToken(username);
        // then
        String result = tokenRepository.getRefreshToken(username);
        assertThat(result).isEqualTo(null);
    }

    @DisplayName("username가 다르면 null을 반환한다.")
    @Test
    void differentUserIdTest(){
        // given
        String username = "testUsername";
        String refreshToken = "refreshToken";

        // when
        tokenRepository.saveRefreshToken(username, refreshToken);

        // then
        String result = tokenRepository.getRefreshToken("testUsername1");
        assertThat(result).isEqualTo(null);
    }
}