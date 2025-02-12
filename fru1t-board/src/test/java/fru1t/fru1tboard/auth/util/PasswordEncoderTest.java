package fru1t.fru1tboard.auth.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


class PasswordEncoderTest {
    private final PasswordEncoder passwordEncoder = new PasswordEncoder();
    
    @DisplayName("비밀번호는 평문과 달라야한다.")
    @Test
    void passwordEncryptionTest(){
        String rawPassword = "password123";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        assertThat(encodedPassword).isNotEqualTo(rawPassword);
    }
    
    @DisplayName("비밀번호는 검증이 가능해야한다.")
    @Test
    void passwordMatchTest(){
        // given
        String rawPassword1 = "password123";
        String rawPassword2 = "pass123";
        
        //then 
        String encodedPassword1 = passwordEncoder.encode(rawPassword1);
        String encodedPassword2 = passwordEncoder.encode(rawPassword2);
        
        //when
        Boolean result1 = passwordEncoder.matches(rawPassword1, encodedPassword1);
        Boolean result2 = passwordEncoder.matches(rawPassword2, encodedPassword2);
        Boolean result3 = passwordEncoder.matches(rawPassword1, encodedPassword2);
        Boolean result4 = passwordEncoder.matches(rawPassword2, encodedPassword1);
        assertThat(result1).isTrue();
        assertThat(result2).isTrue();
        assertThat(result3).isFalse();
        assertThat(result4).isFalse();
    }
    @DisplayName("BCrypt로 암호화된 비밀번호의 길이는 60으로 고정되어있어야한다.")
    @Test
    void passwordFixedLengthTest(){
        //given
        String rawPassword1 = "password123";
        String rawPassword2 = "pass123";
        String rawPassword3 = "ab12";

        //then
        String encodedPassword1 = passwordEncoder.encode(rawPassword1);
        String encodedPassword2 = passwordEncoder.encode(rawPassword2);
        String encodedPassword3 = passwordEncoder.encode(rawPassword3);

        //when
        assertThat(encodedPassword1.length()).isEqualTo(60);
        assertThat(encodedPassword2.length()).isEqualTo(60);
        assertThat(encodedPassword3.length()).isEqualTo(60);
    }
}