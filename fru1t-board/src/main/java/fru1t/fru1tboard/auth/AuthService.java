package fru1t.fru1tboard.auth;

import fru1t.fru1tboard.auth.dto.LoginRequest;
import fru1t.fru1tboard.auth.dto.RefreshRequest;
import fru1t.fru1tboard.auth.dto.TokenResponse;
import fru1t.fru1tboard.auth.util.PasswordEncoder;
import fru1t.fru1tboard.user.UserRepository;
import fru1t.fru1tboard.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtUtil jwtUtil = new JwtUtil();
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;

    public TokenResponse login(LoginRequest request) {
        User user = findUser(request.getUsername());

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        tokenRepository.deleteRefreshToken(user.getUsername());

        String refreshToken = jwtUtil.generateRefreshToken(user.getUsername());
        String accessToken = jwtUtil.generateAccessToken(user.getUsername());

        tokenRepository.saveRefreshToken(user.getUsername(),refreshToken);

        return  TokenResponse.withBothTokens(accessToken, refreshToken);
    }

    public void logout(String token) {
        String username = jwtUtil.getUsernameFromToken(token);
        tokenRepository.deleteRefreshToken(username);
    }

    public TokenResponse refreshToken(RefreshRequest request) {
        String storedRefreshToken = tokenRepository.getRefreshToken(request.getUsername());

        if (storedRefreshToken == null || !storedRefreshToken.equals(request.getRefreshToken())) {
            throw new RuntimeException("Invalid or expired refresh token");
        }

        String newAccessToken = jwtUtil.generateAccessToken(request.getUsername());
        return TokenResponse.withAccessTokenOnly(newAccessToken);
    }

    private User findUser(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}