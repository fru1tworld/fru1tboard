package fru1t.fru1tboard.user;

import fru1t.fru1tboard.auth.util.PasswordEncoder;
import fru1t.fru1tboard.common.Snowflake;
import fru1t.fru1tboard.user.entity.User;
import fru1t.fru1tboard.user.request.SignUpForm;
import fru1t.fru1tboard.user.request.UserUpdateRequest;
import fru1t.fru1tboard.user.response.UserResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final Snowflake snowflake = new Snowflake();
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Transactional
    public UserResponse create(SignUpForm request) {
        User user = userRepository.save(User.create(
                snowflake.nextId(),
                request.getEmail(),
                request.getUsername(),
                passwordEncoder.encode(request.getPassword())
        ));
        return UserResponse.from(user);
    }

    public UserResponse read(Long userId){
        User user = userRepository.findById(userId).orElseThrow();
        return UserResponse.from(user);
    }
    @Transactional
    public UserResponse update(UserUpdateRequest request) {
        User user = userRepository.findById(request.getUserId()).orElseThrow();
        user.update(request);
        return UserResponse.from(user);
    }
    @Transactional
    public void delete(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        userRepository.delete(user);
    }

}