package fru1t.fru1tboard.user;

import fru1t.fru1tboard.user.request.SignUpForm;
import fru1t.fru1tboard.user.request.UserUpdateRequest;
import fru1t.fru1tboard.user.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/")
public class UserController {
    private final UserService userService;

    @PostMapping("v1/singup/")
    public UserResponse signUp(@RequestBody SignUpForm request) {
        UserResponse response = userService.create(request);
        return response;
    }

    @GetMapping("v1/users/{userId}")
    public UserResponse read(@PathVariable Long userId) {
        UserResponse response = userService.read(userId);
        return response;
    }
    @DeleteMapping("v1/users/{userId}")
    public UserResponse delete(@PathVariable Long userId) {
        UserResponse response = userService.read(userId);
        return response;
    }
    @PutMapping("v1/users/")
    public UserResponse update(@RequestBody UserUpdateRequest request) {
        UserResponse response = userService.update(request);
        return response;
    }
}
