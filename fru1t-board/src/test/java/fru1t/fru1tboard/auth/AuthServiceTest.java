package fru1t.fru1tboard.auth;

import fru1t.fru1tboard.auth.dto.LoginRequest;
import fru1t.fru1tboard.user.UserRepository;
import fru1t.fru1tboard.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {
    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    @DisplayName("잘못된 비밀번호로 로그인하면 예외가 발생해야 한다.")
    void login_ShouldThrowException_WhenIncorrectPassword() {
        // Given
        String username = "testUser";
        String wrongPassword = "wrongPassword";

        LoginRequest loginRequest = new LoginRequest(username, wrongPassword);

        User mockUser = mock(User.class);
        when(mockUser.getUsername()).thenReturn(username);
        when(mockUser.getPassword()).thenReturn("correctPassword");

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> authService.login(loginRequest));
        assertEquals("Invalid password", exception.getMessage());
    }


}
